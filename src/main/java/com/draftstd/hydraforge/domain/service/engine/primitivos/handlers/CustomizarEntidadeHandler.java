package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeSistema;
import com.draftstd.hydraforge.domain.service.engine.primitivos.HandlerRegistry;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.*;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeSistemaOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomizarEntidadeHandler implements EtapaHandler {

    private final EntidadeSistemaOutputPort entidadeSistemaPort;
    private final InstanciaResolver instanciaResolver;
    private final EntidadeInstanciaOutputPort instanciaPort;
    private final ObjectMapper mapper;
    private final HandlerRegistry handlers;

    @Autowired
    public CustomizarEntidadeHandler(
            EntidadeSistemaOutputPort entidadeSistemaPort,
            InstanciaResolver instanciaResolver,
            EntidadeInstanciaOutputPort instanciaPort,
            @Lazy HandlerRegistry handlers, 
            ObjectMapper mapper) {
        this.entidadeSistemaPort = entidadeSistemaPort;
        this.instanciaResolver = instanciaResolver;
        this.instanciaPort = instanciaPort;
        this.handlers = handlers;
        this.mapper = mapper;
    }

    @Override
    public String tipoEtapa() { return "CUSTOMIZAR_ENTIDADE"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();
        
        Object idSistemaObj = params.get("id_entidade_sistema");
        UUID idEntidadeSistema = idSistemaObj instanceof UUID u ? u 
                : (idSistemaObj != null ? UUID.fromString(idSistemaObj.toString()) : null);

        String chaveRolagem = (String) params.getOrDefault("chave_rolagem", "customizacao_rolagem");

        EntidadeSistema entidade = entidadeSistemaPort.buscarPorId(idEntidadeSistema)
                .orElseThrow(() -> new IllegalArgumentException("Entidade não encontrada"));

        String chaveTabela = (String) params.getOrDefault("chave_tabela", "tabela_customizacao");
        Map<String, Object> propriedades = entidade.getPropriedades();
        if (propriedades == null || !propriedades.containsKey(chaveTabela)) {
            return ResultadoEtapa.concluida(Map.of("status", "sem_tabela"));
        }

        JsonNode tabelaNode = mapper.valueToTree(propriedades.get(chaveTabela));

        // Se ainda não rolou, solicita ao front
        if (!ctx.getContexto().containsKey(chaveRolagem)) {
            JsonNode dadosNode = tabelaNode.get("dados");
            List<String> dados = new ArrayList<>();
            if (dadosNode != null && dadosNode.isArray()) {
                for (JsonNode d : dadosNode) dados.add(d.asText());
            }

            return ResultadoEtapa.aguardandoInput(Map.of(
                "tipo_input", "ROLAGEM",
                "campoPedido", "Customizar " + entidade.getNome(),
                "salvar_em", chaveRolagem,
                "rolagem", Map.of("dados", dados)
            ));
        }

        // Lê os resultados (ex: [3, 5])
        List<?> rawRolagens = ctx.getContexto().get(chaveRolagem, List.class).orElse(List.of());
        List<Integer> rolagens = new ArrayList<>();
        for (Object r : rawRolagens) {
            rolagens.add(r instanceof Number n ? n.intValue() : Integer.parseInt(r.toString()));
        }

        // Encontra a entrada
        JsonNode entradas = tabelaNode.get("entradas");
        JsonNode entrada = null;
        if (entradas != null && entradas.isArray()) {
            for (JsonNode e : entradas) {
                JsonNode valoresNode = e.get("valores");
                boolean match = true;
                for (int i = 0; i < rolagens.size(); i++) {
                    if (valoresNode.get(i).asInt() != rolagens.get(i)) {
                        match = false;
                        break;
                    }
                }
                if (match) { entrada = e; break; }
            }
        }

        if (entrada == null) {
            return ResultadoEtapa.erro("Nenhuma entrada para " + rolagens);
        }

        // Executa a ação
        JsonNode acoesNode = entrada.get("acao");
        if (acoesNode != null && acoesNode.isArray()) {
            ResultadoEtapa ultimo = null;
            for (JsonNode acao : acoesNode) {
                ultimo = executarAcao(acao, ctx);
                if (ultimo.tipo() == ResultadoEtapa.Tipo.ERRO) {
                    return ultimo; // para na primeira falha
                }
            }
            return ultimo != null ? ultimo : ResultadoEtapa.concluida(Map.of("status", "customizado"));
        } else {
            JsonNode acao = entrada.get("acao");
            if (acao == null) {
                return ResultadoEtapa.erro("Entrada de tabela sem 'acao' nem 'acoes'");
            }
            return executarAcao(acao, ctx);
        }
    }

    private ResultadoEtapa executarAcao(JsonNode acao, ExecucaoContexto ctx) {
        String tipoAcao = acao.get("tipo").asText();
        return switch (tipoAcao) {
            case "customizacao" -> {
                EntidadeInstancia inst = instanciaResolver.retornarAtiva(ctx);
                Map<String, Object> custom = inst.getCustomizacoes() != null 
                        ? new HashMap<>(inst.getCustomizacoes()) 
                        : new HashMap<>();
                
                custom.put(acao.get("chave").asText(), mapper.convertValue(acao.get("valor"), Object.class));
                inst.setCustomizacoes(custom);
                instanciaPort.salvar(inst);
                yield ResultadoEtapa.concluida(Map.of("status", "customizado"));
            }
            case "alterar_atributo" -> {
                EtapaProcedimento sub = new EtapaProcedimento(
                        UUID.randomUUID(), null, 1, "SubEtapaAlterarAtributo", "ALTERAR_ATRIBUTO",
                        Map.of(
                            "atributo", acao.get("atributo").asText(),
                            "operacao", acao.has("operacao") ? acao.get("operacao").asText() : "set",
                            "quantidade", acao.get("quantidade").asDouble()
                        ),
                        true
                );
                yield handlers.get("ALTERAR_ATRIBUTO").executar(sub, ctx);
            }
            case "gerenciar_item" -> {
                EtapaProcedimento sub = new EtapaProcedimento(
                        UUID.randomUUID(), null, 1, "SubEtapaGerenciarItem", "GERENCIAR_ITEM",
                        Map.of(
                            "tipo", acao.has("modo") ? acao.get("modo").asText() : "CRIAR",
                            "opcao_fonte_item", "estatico",
                            "opcao_item", acao.get("item_id").asText(),
                            "fonte_qtd", "estatico",
                            "opcao_qtd", "1",
                            "opcao_fonte_personagem", "instancia_ativa"
                        ),
                        true
                );
                yield handlers.get("GERENCIAR_ITEM").executar(sub, ctx);
            }
            case "chamar_procedimento" -> {
                if (!(ctx instanceof ProcedimentoContexto procedimentoCtx)) {
                    yield ResultadoEtapa.erro("Precisa ser um ProcedimentoContexto");
                }
                JsonNode idProcNode = acao.get("id_procedimento");
                UUID idProcedimento = idProcNode != null && !idProcNode.isNull() 
                        ? UUID.fromString(idProcNode.asText()) 
                        : null;

                EtapaProcedimento sub = new EtapaProcedimento(
                        UUID.randomUUID(), null, 1, "SubEtapaChamarProcedimento", "CHAMAR_PROCEDIMENTO",
                        Map.of(
                            "id_procedimento", idProcedimento != null ? idProcedimento : "",
                            "salvar_em", "resultado_custom",
                            "escopo", "HERDAR"
                        ),
                        true
                );
                yield handlers.get("CHAMAR_PROCEDIMENTO").executar(sub, procedimentoCtx);
            }
            default -> ResultadoEtapa.erro("Ação desconhecida: " + tipoAcao);
        };
    }
}