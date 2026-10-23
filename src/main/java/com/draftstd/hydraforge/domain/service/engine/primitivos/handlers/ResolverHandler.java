package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.mecanica.Resolucao;
import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.primitivos.HandlerRegistry;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.mecanica.ResolutionEvaluator;
import com.draftstd.hydraforge.domain.service.mecanica.ResolutionOutcome;
import com.draftstd.hydraforge.ports.out.persistence.mecanica.ResolucaoOutputPort;
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
public class ResolverHandler implements EtapaHandler {

    private final ResolucaoOutputPort resolucaoPort;
    private final ResolutionEvaluator evaluator;
    private final InstanciaResolver instanciaResolver;
    private final GerenciarItemHandler gerenciarItem;
    private final ChamarProcedimentoHandler chamarProc;
    private final ObjectMapper mapper;
    private final HandlerRegistry handlers;

    @Autowired
    public ResolverHandler(
        ResolucaoOutputPort resolucaoPort,
        ResolutionEvaluator evaluator,
        InstanciaResolver instanciaResolver,
        GerenciarItemHandler gerenciarItem,
        ChamarProcedimentoHandler chamarProc,
        @Lazy HandlerRegistry handlers,
        ObjectMapper mapper) {

        this.resolucaoPort = resolucaoPort;
        this.evaluator = evaluator;
        this.instanciaResolver = instanciaResolver;
        this.gerenciarItem = gerenciarItem;
        this.chamarProc = chamarProc;
        this.handlers = handlers;
        this.mapper = mapper;
    }

    @Override
    public String tipoEtapa() { return "RESOLVER"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();
        
        Object rawIdResolucao = params.get("id_resolucao");
        UUID idResolucao = rawIdResolucao instanceof UUID u ? u : UUID.fromString(rawIdResolucao.toString());

        String salvarResultadoEm = (String) params.get("salvar_resultado_em");
        String modoEsperado = (String) params.get("modo_esperado");

        if (modoEsperado != null) {
            String modoAtual = ctx.getContexto().getStringOrThrow("modo_escolha_cavaleiro");
            if (!modoEsperado.equals(modoAtual)) {
                return ResultadoEtapa.concluida(Map.of("pulado", true));
            }
        }

        Resolucao resolucao = resolucaoPort.buscarPorId(idResolucao)
                .orElseThrow(() -> new IllegalArgumentException("Resolução não encontrada: " + idResolucao));

        // Converte a String JSON (parametrosJson) em JsonNode para manipulação de campos
        JsonNode resolucaoParams;
        try {
            String jsonStr = resolucao.getParametrosJson();
            resolucaoParams = (jsonStr != null && !jsonStr.isBlank()) 
                    ? mapper.readTree(jsonStr) 
                    : mapper.createObjectNode();
        } catch (Exception e) {
            return ResultadoEtapa.erro("Erro ao fazer parse dos parametrosJson da resolução: " + e.getMessage());
        }

        // Se a resolução precisa de uma rolagem ainda não fornecida, solicita ao front
        String chaveRolagem = resolucaoParams.has("chave_rolagem") ? resolucaoParams.get("chave_rolagem").asText() : null;

        if (chaveRolagem != null && !ctx.getContexto().containsKey(chaveRolagem)) {
            Map<String, Object> rolagemConfig = buildRolagemConfig(resolucaoParams);
            return ResultadoEtapa.aguardandoInput(Map.of(
                "tipo_input", "ROLAGEM",
                "campoPedido", resolucao.getNome(),
                "salvar_em", chaveRolagem,
                "rolagem", rolagemConfig
            ));
        }

        // Monta o mapa de contexto exigido pela assinatura do ResolutionEvaluator
        Map<String, Object> mapaContexto = buildMapaContexto(ctx);

        // Avalia a resolução usando a assinatura exata: evaluate(Resolucao, Map<String, Object>)
        ResolutionOutcome outcome = evaluator.evaluate(resolucao, mapaContexto);

        if (salvarResultadoEm != null) {
            ctx.getContexto().put(salvarResultadoEm, outcome.success());
        }

        // Aplica ações da tabela, se houver
        return aplicarAcoes(outcome, resolucaoParams, ctx, salvarResultadoEm);
    }

    private ResultadoEtapa aplicarAcoes(ResolutionOutcome outcome, JsonNode params, ExecucaoContexto ctx, String salvarResultadoEm) {
        JsonNode tabela = params.get("tabela");
        if (tabela == null || !tabela.isArray()) {
            return ResultadoEtapa.concluida(Map.of("sucesso", outcome.success(), "motivo", outcome.motivo()));
        }

        // Localiza a entrada correspondente ao resultado
        Object rollValue = outcome.roll();
        JsonNode entrada = null;

        if (rollValue instanceof Integer roll) {
            entrada = findTabelaEntrada(tabela, roll);
        } else if (rollValue instanceof List<?> list && list.size() == 2) {
            int r1 = ((Number) list.get(0)).intValue();
            int r2 = ((Number) list.get(1)).intValue();
            entrada = findTabelaDuplaEntrada(tabela, r1, r2);
        }

        if (entrada == null) {
            return ResultadoEtapa.erro("Nenhuma entrada na tabela para o resultado " + rollValue);
        }

        if (entrada.has("id_entidade")) {
            String rawIdEntidade = entrada.get("id_entidade").asText();
            UUID idEntidade = UUID.fromString(rawIdEntidade);
            if (salvarResultadoEm != null) {
                ctx.getContexto().put(salvarResultadoEm, idEntidade);
            }
            return ResultadoEtapa.concluida(Map.of("entidade_selecionada", idEntidade));
        }

        if (entrada.has("id_condicao")) {
            String idCondicao = entrada.get("id_condicao").asText();
            Map<String, Object> itemParams = Map.of(
                "tipo", "CRIAR",
                "opcao_fonte_item", "estatico",
                "opcao_item", idCondicao,
                "fonte_qtd", "estatico",
                "opcao_qtd", "1",
                "opcao_fonte_personagem", "instancia_ativa"
            );
            EtapaProcedimento subEtapa = new EtapaProcedimento(
                    UUID.randomUUID(), null, 1, "SubEtapaGerenciarItem", "GERENCIAR_ITEM", itemParams, true
            );
            return gerenciarItem.executar(subEtapa, ctx);
        }

        if (entrada.has("id_procedimento")) {
            String rawIdProc = entrada.get("id_procedimento").asText();
            UUID idProc = UUID.fromString(rawIdProc);
            Map<String, Object> procParams = Map.of(
                "id_procedimento", idProc,
                "salvar_em", "resultado_scar",
                "escopo", "HERDAR"
            );
            EtapaProcedimento subEtapa = new EtapaProcedimento(
                    UUID.randomUUID(), null, 1, "SubEtapaChamarProcedimento", "CHAMAR_PROCEDIMENTO", procParams, true
            );
            return chamarProc.executar(subEtapa, (ProcedimentoContexto) ctx);
        }

        if (entrada.has("acao")) {
            JsonNode acao = entrada.get("acao");
            String tipoEtapa = acao.get("tipoEtapa").asText();
            Map<String, Object> paramsEtapa = mapper.convertValue(acao.get("parametros"), Map.class);
            
            EtapaProcedimento etapaDinamica = new EtapaProcedimento(
                    UUID.randomUUID(), null, 1, "SubEtapaDinamica", tipoEtapa, paramsEtapa, true
            );
            
            EtapaHandler handler = handlers.get(tipoEtapa);
            if (handler == null) return ResultadoEtapa.erro("Handler não encontrado: " + tipoEtapa);
            
            return handler.executar(etapaDinamica, ctx);
        }

        return ResultadoEtapa.concluida(Map.of("sucesso", true));
    }

    private Map<String, Object> buildMapaContexto(ExecucaoContexto ctx) {
        Map<String, Object> map = new HashMap<>();
        for (String key : ctx.getContexto().keySet()) {
            ctx.getContexto().get(key, Object.class).ifPresent(val -> map.put(key, val));
        }
        if (ctx.temInstanciaUnica()) {
            EntidadeInstancia inst = instanciaResolver.retornarAtiva(ctx);
            map.put("atributos", inst.getValoresAtributos());
        }
        return map;
    }

    private Map<String, Object> buildRolagemConfig(JsonNode params) {
        JsonNode dadosNode = params.get("dados");
        List<String> dados = new ArrayList<>();
        if (dadosNode != null && dadosNode.isArray()) {
            for (JsonNode d : dadosNode) dados.add(d.asText());
        } else {
            dados.add("d20");
        }
        return Map.of("dados", dados);
    }

    private JsonNode findTabelaEntrada(JsonNode tabela, int valor) {
        if (tabela == null || !tabela.isArray()) return null;
        for (JsonNode entry : tabela) {
            if (entry.has("min") && entry.has("max")) {
                if (valor >= entry.get("min").asInt() && valor <= entry.get("max").asInt()) return entry;
            } else if (entry.has("valor") && entry.get("valor").asInt() == valor) return entry;
        }
        return null;
    }

    private JsonNode findTabelaDuplaEntrada(JsonNode tabela, int roll1, int roll2) {
        if (tabela == null || !tabela.isArray()) return null;
        for (JsonNode entry : tabela) {
            if (entry.has("d6") && entry.has("d12")) {
                if (entry.get("d6").asInt() == roll1 && entry.get("d12").asInt() == roll2) return entry;
            } else if (entry.has("valor1") && entry.has("valor2")) {
                if (entry.get("valor1").asInt() == roll1 && entry.get("valor2").asInt() == roll2) return entry;
            }
        }
        return null;
    }
}