package com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto;

import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto.Contexto;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class InterpretadorContexto implements Contexto {

    private final ProcedimentoContexto ctx;
    private final InstanciaResolver instanciaResolver;
    private final CenaPersistencePort cenaPort;
    private final JsonMapper mapper;

    private List<EntidadeInstancia> inimigosCache;
    private List<EntidadeInstancia> aliadosCache;
    private List<EntidadeInstancia> participantesCache;

    @Override
    public Optional<Object> get(String caminho) {
        try {
            return Optional.ofNullable(resolver(caminho));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Object resolver(String caminho) {
        if ("iteracao".equals(caminho)) {
            return ctx.getIteracaoAtual();
        }

        if (caminho.startsWith("cena.")) {
            return resolverCena(caminho.substring("cena.".length()));
        }

        if (caminho.startsWith("contexto.")) {
            String chave = caminho.substring("contexto.".length());
            return ctx.getContexto().get(chave, Object.class).orElse(null);
        }

        if (caminho.startsWith("instancia.")) {
            String atributo = caminho.substring("instancia.".length());
            if (!ctx.temInstanciaUnica()) return null;
            EntidadeInstancia inst = instanciaResolver.retornarAtiva(ctx);
            return inst.getValoresAtributos() != null ? inst.getValoresAtributos().get(atributo) : null;
        }

        if ("inimigos".equals(caminho) || "inimigos.*".equals(caminho)) {
            return asAtributosMap(getInimigos());
        }
        if ("aliados".equals(caminho) || "aliados.*".equals(caminho)) {
            return asAtributosMap(getAliados());
        }
        if ("participantes".equals(caminho) || "participantes.*".equals(caminho)) {
            return asAtributosMap(getParticipantes());
        }

        return null;
    }

    private List<Map<String, Object>> asAtributosMap(List<EntidadeInstancia> instancias) {
        return instancias.stream()
                .map(i -> mapper.convertValue(
                        i.getValoresAtributos(), new TypeReference<Map<String, Object>>() {}))
                .toList();
    }

    private Object resolverCena(String sub) {
        Object idCenaObj = ctx.getContexto().get("id_cena", Object.class).orElse(null);
        if (idCenaObj == null) return null;
        
        UUID idCena = null;
        if (idCenaObj instanceof UUID u) {
            idCena = u;
        } else {
            try {
                idCena = UUID.fromString(idCenaObj.toString());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        Cena cena = cenaPort.buscarPorId(idCena).orElse(null);
        if (cena == null || cena.getEstadoJson() == null || cena.getEstadoJson().isBlank()) return null;

        try {
            Map<String, Object> estado = mapper.readValue(cena.getEstadoJson(), new TypeReference<Map<String, Object>>() {});
            return switch (sub) {
                case "rodada" -> estado.get("rodada");
                case "status" -> {
                    if (estado.containsKey("status")) {
                        yield estado.get("status");
                    }
                    if (estado.containsKey("combateAtivo")) {
                        Boolean ativo = (Boolean) estado.get("combateAtivo");
                        yield Boolean.TRUE.equals(ativo) ? "EM_ANDAMENTO" : "CONCLUIDO";
                    }
                    yield null;
                }
                case "turnoAtualId" -> estado.get("turnoAtualId");
                case "jaAgiramIds" -> estado.get("jaAgiramIds");
                case "dadosDisponiveis" -> estado.get("dadosDisponiveis");
                default -> estado.get(sub);
            };
        } catch (Exception e) {
            return null;
        }
    }

    private List<EntidadeInstancia> getInimigos() {
        if (inimigosCache == null)
            inimigosCache = instanciaResolver.resolverDeFonte("cena.inimigos", ctx);
        return inimigosCache;
    }

    private List<EntidadeInstancia> getAliados() {
        if (aliadosCache == null)
            aliadosCache = instanciaResolver.resolverDeFonte("cena.aliados", ctx);
        return aliadosCache;
    }

    private List<EntidadeInstancia> getParticipantes() {
        if (participantesCache == null)
            participantesCache = instanciaResolver.resolverDeFonte("cena.todos", ctx);
        return participantesCache;
    }
}