package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeRelacao;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeSistema;
import com.draftstd.hydraforge.domain.service.engine.components.ItemResolver;
import com.draftstd.hydraforge.domain.service.engine.primitivos.HandlerRegistry;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.personagem.ItemEfeitoService;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeRelacaoOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeSistemaOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class GerenciarItemHandler implements EtapaHandler {

    private final InstanciaResolver instanciaResolver;
    private final ItemResolver itemResolver;
    private final ItemEfeitoService itemEfeitoService;
    private final EntidadeRelacaoOutputPort entidadeRelacaoPort;
    private final EntidadeInstanciaOutputPort entidadeInstanciaPort;
    private final EntidadeSistemaOutputPort entidadeSistemaPort;
    
    private HandlerRegistry handlers;

    public GerenciarItemHandler(InstanciaResolver instanciaResolver,
                                ItemResolver itemResolver,
                                ItemEfeitoService itemEfeitoService,
                                EntidadeRelacaoOutputPort entidadeRelacaoPort,
                                EntidadeInstanciaOutputPort entidadeInstanciaPort,
                                EntidadeSistemaOutputPort entidadeSistemaPort) {
        this.instanciaResolver = instanciaResolver;
        this.itemResolver = itemResolver;
        this.itemEfeitoService = itemEfeitoService;
        this.entidadeRelacaoPort = entidadeRelacaoPort;
        this.entidadeInstanciaPort = entidadeInstanciaPort;
        this.entidadeSistemaPort = entidadeSistemaPort;
    }

    @Lazy
    @Autowired
    public void setHandlers(HandlerRegistry handlers) {
        this.handlers = handlers;
    }

    @Override
    public String tipoEtapa() { return "GERENCIAR_ITEM"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        String tipo = (String) params.get("tipo");

        UUID idItem = resolverIdItem(params, ctx);
        List<EntidadeInstancia> personagens = resolverPersonagens(params, ctx);

        EntidadeInstancia item = entidadeInstanciaPort.buscarPorId(idItem)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado: " + idItem));

        return switch (tipo) {
            case "CRIAR"       -> inserir(personagens, item, params, ctx);
            case "REMOVER"     -> remover(personagens, item, params);
            case "EQUIPAR"     -> equipar(personagens, item, ctx);
            case "DESEQUIPAR"  -> desequipar(personagens, item, ctx);
            default -> ResultadoEtapa.erro("tipo de GERENCIAR_ITEM desconhecido: " + tipo);
        };
    }

    private ResultadoEtapa inserir(List<EntidadeInstancia> personagens,
                                 EntidadeInstancia item,
                                 Map<String, Object> params,
                                 ExecucaoContexto ctx) {
        int qtd = resolverQtd(params, ctx);
        Map<UUID, Integer> resultado = new LinkedHashMap<>();

        for (EntidadeInstancia personagem : personagens) {
            Optional<EntidadeRelacao> existente =
                    itemResolver.buscarRelacao(personagem.getId(), item.getId());

            int novaQtd;
            if (existente.isPresent()) {
                EntidadeRelacao rel = existente.get();
                novaQtd = rel.getQuantidade() + qtd;
                rel.setQuantidade(novaQtd);
                entidadeRelacaoPort.salvar(rel);
            } else {
                EntidadeRelacao rel = new EntidadeRelacao();
                rel.setId(UUID.randomUUID());
                rel.setEntidadePaiId(personagem.getId());
                rel.setEntidadeFilhaId(item.getId());
                rel.setQuantidade(qtd);
                entidadeRelacaoPort.salvar(rel);
                novaQtd = qtd;
            }
            resultado.put(personagem.getId(), novaQtd);
        }

        ResultadoEtapa customizacao = dispararCustomizacoes(item, ctx);
        if (customizacao != null) return customizacao;

        return ResultadoEtapa.concluida(Map.of(
                "tipo", "CRIAR", "idItem", item.getId(), "quantidades", resultado));
    }

    private ResultadoEtapa dispararCustomizacoes(EntidadeInstancia item, ExecucaoContexto ctx) {
        if (item.getEntidadeSistemaId() == null) return null;

        Optional<EntidadeSistema> optTemplate = entidadeSistemaPort.buscarPorId(item.getEntidadeSistemaId());
        if (optTemplate.isEmpty()) return null;

        EntidadeSistema template = optTemplate.get();
        Map<String, Object> propriedades = template.getPropriedades();
        if (propriedades == null || propriedades.isEmpty()) return null;

        for (Map.Entry<String, Object> entry : propriedades.entrySet()) {
            if (!entry.getKey().startsWith("tabela_customizacao_")) continue;

            String chaveTabela = entry.getKey();
            String chaveRolagem = "customizacao_" + item.getId() + "_" + chaveTabela;
            String chaveConcluida = chaveRolagem + "_concluida";

            if (ctx.getContexto().containsKey(chaveConcluida)) continue;

            EtapaProcedimento sub = new EtapaProcedimento(
                    UUID.randomUUID(),
                    null,
                    1,
                    "Customizar " + template.getNome() + " — " + chaveTabela,
                    "CUSTOMIZAR_ENTIDADE",
                    Map.of(
                            "id_entidade_sistema", template.getId(),
                            "chave_rolagem",       chaveRolagem,
                            "chave_tabela",        chaveTabela
                    ),
                    true
            );

            ResultadoEtapa resultado = handlers.get("CUSTOMIZAR_ENTIDADE").executar(sub, ctx);

            if (resultado.tipo() == ResultadoEtapa.Tipo.AGUARDANDO_INPUT
            || resultado.tipo() == ResultadoEtapa.Tipo.ERRO) {
                return resultado;
            }

            ctx.getContexto().put(chaveConcluida, true);
        }
        return null;
    }

    private ResultadoEtapa remover(List<EntidadeInstancia> personagens,
                                   EntidadeInstancia item,
                                   Map<String, Object> params) {
        int qtd = resolverQtd(params, null);
        Map<UUID, Integer> resultado = new LinkedHashMap<>();

        for (EntidadeInstancia personagem : personagens) {
            EntidadeRelacao rel = itemResolver
                    .buscarRelacao(personagem.getId(), item.getId())
                    .orElse(null);

            if (rel == null) {
                resultado.put(personagem.getId(), 0);
                continue;
            }

            int restante = rel.getQuantidade() - qtd;
            if (restante <= 0) {
                entidadeRelacaoPort.deletarPorId(rel.getId());
                resultado.put(personagem.getId(), 0);
            } else {
                rel.setQuantidade(restante);
                entidadeRelacaoPort.salvar(rel);
                resultado.put(personagem.getId(), restante);
            }
        }

        return ResultadoEtapa.concluida(Map.of(
                "tipo", "REMOVER", "idItem", item.getId(), "quantidades", resultado));
    }

    private ResultadoEtapa equipar(List<EntidadeInstancia> personagens,
                                   EntidadeInstancia item,
                                   ExecucaoContexto ctx) {
        String slot = itemResolver.slotDoItem(item);
        if (slot == null) {
            return ResultadoEtapa.erro("Item " + item.getId() + " não possui slot — não pode ser equipado");
        }

        EntidadeSistema templateItem = item.getEntidadeSistemaId() != null 
                ? entidadeSistemaPort.buscarPorId(item.getEntidadeSistemaId()).orElse(null) 
                : null;

        for (EntidadeInstancia personagem : personagens) {
            EntidadeRelacao rel = itemResolver
                    .buscarRelacao(personagem.getId(), item.getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Personagem " + personagem.getId() + " não possui o item " + item.getId()));

            itemResolver.buscarEquipadoPorSlot(personagem.getId(), slot)
                    .ifPresent(itemAtual -> {
                        if (!itemAtual.getId().equals(item.getId())) {
                            EntidadeRelacao relAtual = itemResolver
                                    .buscarRelacao(personagem.getId(), itemAtual.getId())
                                    .orElse(null);
                            if (relAtual != null) {
                                setEquipado(relAtual, false);
                            }
                            
                            EntidadeSistema templateDesequipado = (itemAtual.getEntidadeSistemaId() != null)
                                    ? entidadeSistemaPort.buscarPorId(itemAtual.getEntidadeSistemaId()).orElse(null)
                                    : null;

                            itemEfeitoService.dispararGatilho(
                                    itemAtual, templateDesequipado, "AO_DESEQUIPAR", personagem, personagem, Map.of());
                        }
                    });

            setEquipado(rel, true);
            itemEfeitoService.dispararGatilho(item, templateItem, "AO_EQUIPAR", personagem, personagem, Map.of());
        }

        return ResultadoEtapa.concluida(Map.of(
                "tipo", "EQUIPAR", "idItem", item.getId(), "slot", slot));
    }

    private ResultadoEtapa desequipar(List<EntidadeInstancia> personagens,
                                      EntidadeInstancia item,
                                      ExecucaoContexto ctx) {
        EntidadeSistema templateItem = item.getEntidadeSistemaId() != null 
                ? entidadeSistemaPort.buscarPorId(item.getEntidadeSistemaId()).orElse(null) 
                : null;

        for (EntidadeInstancia personagem : personagens) {
            EntidadeRelacao rel = itemResolver
                    .buscarRelacao(personagem.getId(), item.getId())
                    .orElseThrow();

            setEquipado(rel, false);
            itemEfeitoService.dispararGatilho(item, templateItem, "AO_DESEQUIPAR", personagem, personagem, Map.of());
        }

        return ResultadoEtapa.concluida(Map.of("tipo", "DESEQUIPAR", "idItem", item.getId()));
    }

    private void setEquipado(EntidadeRelacao rel, boolean valor) {
        Map<String, Object> custom = rel.getCustomizacoes() != null 
                ? new HashMap<>(rel.getCustomizacoes()) 
                : new HashMap<>();
        custom.put("equipado", valor);
        rel.setCustomizacoes(custom);
        entidadeRelacaoPort.salvar(rel);
    }

    private UUID resolverIdItem(Map<String, Object> params, ExecucaoContexto ctx) {
        String fonte  = (String) params.get("opcao_fonte_item");
        Object opcao  = params.get("opcao_item");
        if ("estatico".equals(fonte)) {
            return opcao instanceof UUID u ? u : UUID.fromString(opcao.toString());
        }
        if ("contexto".equals(fonte)) {
            Object val = ctx.getContexto().get(opcao.toString(), Object.class).orElseThrow();
            return val instanceof UUID u ? u : UUID.fromString(val.toString());
        }
        throw new IllegalArgumentException("opcao_fonte_item inválida: " + fonte);
    }

    private int resolverQtd(Map<String, Object> params, ExecucaoContexto ctx) {
        String fonte = (String) params.get("fonte_qtd");
        String opcao = (String) params.get("opcao_qtd");
        if ("estatico".equals(fonte)) return Integer.parseInt(opcao);
        if ("contexto".equals(fonte)) return ctx.getContexto().getInt(opcao).orElseThrow();
        throw new IllegalArgumentException("fonte_qtd inválida: " + fonte);
    }

    private List<EntidadeInstancia> resolverPersonagens(Map<String, Object> params,
                                                        ExecucaoContexto ctx) {
        String fonte = (String) params.get("opcao_fonte_personagem");
        if ("instancia_ativa".equals(fonte)) return List.of(instanciaResolver.retornarAtiva(ctx));
        if (fonte.startsWith("batalha.")) return instanciaResolver.resolverDeFonte(fonte, ctx);
        throw new IllegalArgumentException("opcao_fonte_personagem inválida: " + fonte);
    }
}