package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.components.ItemResolver;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.engine.utils.rolagemEngine.Rolagem;
import com.draftstd.hydraforge.domain.service.engine.utils.rolagemEngine.RolagemEngine;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ResolverAtaquesAgrupadosHandler implements EtapaHandler {

    private final RolagemEngine rolagemEngine;
    private final ItemResolver itemResolver;
    private final EntidadeInstanciaOutputPort instanciaPort;

    @Override
    public String tipoEtapa() { return "RESOLVER_ATAQUES_AGRUPADOS"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        String declaracoesFonte  = exigirTexto(params, "declaracoes_fonte", etapa);
        Object origemDadoAtaque  = exigirNo(params, "dado_ataque", etapa);
        Object origemReducao     = params.get("reducao_dano"); // opcional

        int limiarGambit        = params.containsKey("limiar_gambit") ? Integer.parseInt(params.get("limiar_gambit").toString()) : 4;
        String trackDano        = (String) params.getOrDefault("track_dano", "guard");
        String overflowPara     = (String) params.get("overflow_para");
        String salvarResultados = (String) params.get("salvar_resultados_em");
        String salvarGambits    = (String) params.get("salvar_gambits_em");
        String salvarFeridosEm  = (String) params.get("salvar_alvos_feridos_em");

        Map<UUID, UUID> declaracoes = lerDeclaracoes(declaracoesFonte, ctx, etapa);

        Map<UUID, List<UUID>> atacantesPorAlvo = new LinkedHashMap<>();
        for (var e : declaracoes.entrySet()) {
            if (e.getValue() == null) continue; // passou a vez
            atacantesPorAlvo.computeIfAbsent(e.getValue(), k -> new ArrayList<>()).add(e.getKey());
        }

        Map<UUID, Object> resultadosPorAlvo = new LinkedHashMap<>();
        Map<UUID, Object> gambits = new LinkedHashMap<>();
        List<UUID> alvosFeridos = new ArrayList<>();

        for (var grupo : atacantesPorAlvo.entrySet()) {
            UUID idAlvo = grupo.getKey();
            List<UUID> atacantes = grupo.getValue();

            // ── Cada atacante rola com o seu próprio dado de arma ──────────────
            Map<UUID, Integer> rolosPorAtacante = new LinkedHashMap<>();
            for (UUID idAtacante : atacantes) {
                Rolagem rolagem = resolverDadoAtaqueDoAtacante(origemDadoAtaque, idAtacante, etapa);
                int rolo = rolagemEngine.executar(rolagem).total();
                rolosPorAtacante.put(idAtacante, rolo);
            }

            var maxEntry = rolosPorAtacante.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).orElseThrow();
            int danoBruto = maxEntry.getValue();

            // ── A redução é oriunda do equipamento do próprio ALVO ───────────
            int reducao = resolverReducaoDoAlvo(origemReducao, idAlvo);
            int danoFinal = Math.max(0, danoBruto - reducao);

            EntidadeInstancia alvo = instanciaPort.buscarPorId(idAlvo)
                    .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, idAlvo));
            int danoSobrando = aplicarDano(alvo, danoFinal, trackDano, overflowPara);

            resultadosPorAlvo.put(idAlvo, Map.of(
                    "rolosPorAtacante", rolosPorAtacante,
                    "idVencedor", maxEntry.getKey(),
                    "danoBruto", danoBruto,
                    "reducao", reducao,
                    "danoAplicado", danoFinal,
                    "guardRestante", danoSobrando
            ));

            if (danoSobrando < 0 && salvarFeridosEm != null) alvosFeridos.add(idAlvo);

            // Gambits utilizam a rolagem bruta
            for (var rolo : rolosPorAtacante.entrySet()) {
                if (rolo.getValue() > limiarGambit) {
                    gambits.put(rolo.getKey(), Map.of("valorRolado", rolo.getValue(), "idAlvoOriginal", idAlvo));
                }
            }
        }

        if (salvarResultados != null) ctx.getContexto().put(salvarResultados, resultadosPorAlvo);
        if (salvarGambits != null) {
            ctx.getContexto().put(salvarGambits, gambits);
            ctx.getContexto().put(salvarGambits + "_ids", new ArrayList<>(gambits.keySet()));
        }
        if (salvarFeridosEm != null) ctx.getContexto().put(salvarFeridosEm, alvosFeridos);

        return ResultadoEtapa.concluida(Map.of(
                "resultadosPorAlvo", resultadosPorAlvo, "gambitsDisponiveis", gambits.size()));
    }

    // ════════════════════════════════════════════════════════════
    // Resolução de Arma — executada uma vez por atacante
    // ════════════════════════════════════════════════════════════

    private Rolagem resolverDadoAtaqueDoAtacante(Object origemDadoAtaque, UUID idAtacante, EtapaExecutavel etapa) {
        if (origemDadoAtaque instanceof String str) {
            return new Rolagem(str, 1, false);
        }

        if (!(origemDadoAtaque instanceof Map<?, ?> map)) {
            throw new IllegalArgumentException("Origem de dado_ataque inválida");
        }

        Object rawFonte = map.get("fonte");
        String fonte = rawFonte != null ? rawFonte.toString() : "ausente";
        String chave = map.get("chave") != null ? map.get("chave").toString() : null;

        return switch (fonte) {

            case "item_equipado" -> {
                String slot = map.get("slot") != null ? map.get("slot").toString() : null;
                if (slot == null || chave == null) {
                    throw new JsonFieldNotFoundException("slot/chave", etapa.getNome());
                }
                Object dadoAtaque = itemResolver.atributoDoEquipado(idAtacante, slot, "dado_ataque");
                Object dadoQtd = itemResolver.atributoDoEquipado(idAtacante, slot, "dado_qtd");
                if (dadoAtaque == null || dadoQtd == null) {
                    throw new IllegalStateException(
                            "Atacante " + idAtacante + " não possui item equipado no slot '" + slot +
                                    "' (ou o item carece do atributo '" + chave + "')");
                }
                int qtd = dadoQtd instanceof Number n ? n.intValue() : Integer.parseInt(dadoQtd.toString());
                yield new Rolagem(dadoAtaque.toString(), qtd, false);
            }

            case "atributo" -> {
                EntidadeInstancia atacante = instanciaPort.buscarPorId(idAtacante)
                        .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, idAtacante));
                Map<String, Object> attrs = atacante.getValoresAtributos();
                Object valor = attrs != null ? attrs.get(chave) : null;
                if (valor == null) {
                    throw new IllegalStateException("Atacante " + idAtacante + " não possui o atributo '" + chave + "'");
                }
                yield new Rolagem(valor.toString(), 1, false);
            }

            default -> throw new IllegalArgumentException("Fonte desconhecida para dado_ataque: '" + fonte + "'");
        };
    }

    // ════════════════════════════════════════════════════════════
    // Resolução de Armadura/Escudo — executada uma vez por alvo
    // ════════════════════════════════════════════════════════════

    private int resolverReducaoDoAlvo(Object origemReducao, UUID idAlvo) {
        if (origemReducao == null || !(origemReducao instanceof Map<?, ?> map)) return 0;

        Object rawFonte = map.get("fonte");
        String fonte = rawFonte != null ? rawFonte.toString() : "ausente";
        String chave = map.get("chave") != null ? map.get("chave").toString() : null;

        return switch (fonte) {
            case "ausente" -> 0;

            case "item_equipado" -> {
                String slot = map.get("slot") != null ? map.get("slot").toString() : null;
                if (slot == null || chave == null) yield 0;
                Object valorItem = itemResolver.atributoDoEquipado(idAlvo, slot, chave);
                if (valorItem == null) yield 0;
                yield valorItem instanceof Number n ? n.intValue() : Integer.parseInt(valorItem.toString());
            }

            case "atributo" -> {
                if (chave == null) yield 0;
                EntidadeInstancia alvo = instanciaPort.buscarPorId(idAlvo)
                        .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, idAlvo));
                Map<String, Object> attrs = alvo.getValoresAtributos();
                Object valor = attrs != null ? attrs.get(chave) : null;
                if (valor == null) yield 0;
                yield valor instanceof Number n ? n.intValue() : Integer.parseInt(valor.toString());
            }

            case "fixo" -> {
                Object v = map.get("chave");
                if (v == null) yield 0;
                yield v instanceof Number n ? n.intValue() : Integer.parseInt(v.toString());
            }

            default -> throw new IllegalArgumentException("Fonte desconhecida para reducao_dano: '" + fonte + "'");
        };
    }

    // ════════════════════════════════════════════════════════════

    private int aplicarDano(EntidadeInstancia alvo, int dano, String track, String overflow) {
        Map<String, Object> attrs = alvo.getValoresAtributos() != null 
                ? new HashMap<>(alvo.getValoresAtributos()) 
                : new HashMap<>();

        Object valTrack = attrs.get(track);
        int atual = valTrack instanceof Number n ? n.intValue() : (valTrack != null ? Integer.parseInt(valTrack.toString()) : 0);
        int restante = atual - dano;

        if (restante >= 0) {
            attrs.put(track, restante);
        } else {
            attrs.put(track, 0);
            if (overflow != null) {
                Object valOverflow = attrs.get(overflow);
                int overflowAtual = valOverflow instanceof Number n ? n.intValue() : (valOverflow != null ? Integer.parseInt(valOverflow.toString()) : 0);
                attrs.put(overflow, Math.max(0, overflowAtual + restante));
            }
        }
        alvo.setValoresAtributos(attrs);
        instanciaPort.salvar(alvo);
        return restante;
    }

    private Map<UUID, UUID> lerDeclaracoes(String fonte, ExecucaoContexto ctx, EtapaExecutavel etapa) {
        String chave = fonte.startsWith("contexto.") ? fonte.substring("contexto.".length()) : fonte;
        Object raw = ctx.getContexto().get(chave, Object.class)
                .orElseThrow(() -> new JsonFieldNotFoundException(chave, etapa.getNome()));
        if (!(raw instanceof Map<?, ?> rawMap)) {
            throw new IllegalStateException("Esperado um Map em contexto['" + chave + "'], encontrado " + raw.getClass());
        }
        Map<UUID, UUID> out = new LinkedHashMap<>();
        for (var e : rawMap.entrySet()) {
            UUID idAtacante = converterParaUuid(e.getKey());
            UUID idAlvo = converterParaUuid(extrairIdAlvo(e.getValue()));
            if (idAtacante != null) out.put(idAtacante, idAlvo);
        }
        return out;
    }

    private Object extrairIdAlvo(Object declaracao) {
        if (declaracao instanceof Map<?, ?> m) return m.get("id_alvo");
        return declaracao;
    }

    private UUID converterParaUuid(Object obj) {
        if (obj == null) return null;
        if (obj instanceof UUID u) return u;
        try {
            return UUID.fromString(obj.toString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Object exigirNo(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object v = params.get(chave);
        if (v == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return v;
    }

    private String exigirTexto(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object v = params.get(chave);
        if (v == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return v.toString();
    }
}