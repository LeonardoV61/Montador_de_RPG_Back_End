package com.rpgvtt.montador_de_rpg_backend.domain.engine.primitivos;

import com.rpgvtt.montador_de_rpg_backend.domain.engine.utils.ContextoDinamico;
import com.rpgvtt.montador_de_rpg_backend.domain.engine.components.InterpretadorJson;
import com.rpgvtt.montador_de_rpg_backend.domain.engine.utils.ResultadoExpressao;
import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.EtapaProcedimento;
import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.Procedimento;
import com.rpgvtt.montador_de_rpg_backend.domain.validation.schema.AtributoSchema;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.function.Function;

public class ProcedimentoExecutor {

    private final EstadoSessao estadoSessao;
    private final InterpretadorJson interpretador;
    private final Map<String, PrimitivoExecutor> primitivos;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Deque<FrameExecucao> pilha = new ArrayDeque<>();

    private Function<Long, ContextoDinamico> contextoFactory;

    public ProcedimentoExecutor(EstadoSessao estadoSessao,
                                InterpretadorJson interpretador,
                                Map<String, PrimitivoExecutor> primitivos) {
        this.estadoSessao = estadoSessao;
        this.interpretador = interpretador;
        this.primitivos = primitivos;
        // Inicializa a fábrica de contextos assim que tivermos os schemas
        this.contextoFactory = this::criarContextoParaEntidade;
    }

    public void executar(Procedimento procedimento, Long entidadeDona) {
        estadoSessao.setEntidadeAtual(entidadeDona);
        pilha.clear();
        pilha.push(new FrameExecucao(procedimento));
        processar();
    }

    public void retomar() {
        pilha.clear();
        Deque<FrameExecucao> salva = estadoSessao.getPilhaExecucao();
        if (salva != null) {
            pilha.addAll(salva);
        }
        estadoSessao.getSuspensoes().clear();
        FrameExecucao topo = pilha.peek();
        if (topo != null) {
            topo.setIndiceEtapaAtual(topo.getIndiceEtapaAtual() + 1);
        }
        processar();
    }

    private void processar() {
        while (!pilha.isEmpty() && !estadoSessao.estaSuspenso()) {
            FrameExecucao frame = pilha.peek();
            List<EtapaProcedimento> etapas = frame.getProcedimento().getEtapas();
            if (frame.getIndiceEtapaAtual() >= etapas.size()) {
                pilha.pop();
                continue;
            }
            EtapaProcedimento etapa = etapas.get(frame.getIndiceEtapaAtual());
            executarEtapa(etapa, frame);
            // Se a etapa não causou suspensão, avança o ponteiro
            if (!estadoSessao.estaSuspenso()) {
                frame.setIndiceEtapaAtual(frame.getIndiceEtapaAtual() + 1);
            }
        }
        // Salva a pilha se houve suspensão, para futura retomada
        if (estadoSessao.estaSuspenso()) {
            estadoSessao.setPilhaExecucao(new ArrayDeque<>(pilha));
        }
    }

    private void executarEtapa(EtapaProcedimento etapa, FrameExecucao frame) {
        String tipo = etapa.getTipoEtapa();
        JsonNode params = etapa.getParametros_etapa();
        switch (tipo) {
            case "acao"            -> executarAcao(params, frame);
            case "condicao"        -> executarCondicao(params, frame);
            case "loop"            -> executarLoop(params, frame);
            case "subprocedimento" -> executarSubprocedimento(params, frame);
            case "suspensao"       -> executarSuspensao(params, frame);
            default -> throw new IllegalArgumentException("Tipo de etapa desconhecido: " + tipo);
        }
    }

    private void executarAcao(JsonNode params, FrameExecucao frame) {
        String nomePrimitivo = params.get("primitivo").asText();
        JsonNode paramsPrimitivo = params.get("parametros");
        Map<String, Object> paramsResolvidos = resolverParametros(paramsPrimitivo, frame);
        PrimitivoExecutor executor = primitivos.get(nomePrimitivo);
        if (executor == null) {
            throw new IllegalArgumentException("Primitivo não encontrado: " + nomePrimitivo);
        }
        ContextoDinamico contexto = criarContextoParaEntidade(estadoSessao.getEntidadeAtual());

        JsonNode paramsJson = objectMapper.valueToTree(paramsResolvidos);

        executor.executar(paramsJson, contexto, estadoSessao);
    }


    private void executarCondicao(JsonNode params, FrameExecucao frame) {
        JsonNode condicaoNode = params.get("condicao");
        ContextoDinamico ctx = criarContextoParaEntidade(estadoSessao.getEntidadeAtual());
        ResultadoExpressao resultado = interpretador.interpretar(condicaoNode, ctx);
        if (!resultado.comoBooleano()) {
            // Condição falsa: pula para etapa especificada (por nome)
            if (params.has("etapa_se_falso")) {
                String nomeEtapa = params.get("etapa_se_falso").asText();
                List<EtapaProcedimento> etapas = frame.getProcedimento().getEtapas();
                for (int i = 0; i < etapas.size(); i++) {
                    if (etapas.get(i).getNome().equals(nomeEtapa)) {
                        frame.setIndiceEtapaAtual(i);
                        return;
                    }
                }
                throw new IllegalArgumentException("Etapa não encontrada: " + nomeEtapa);
            }
            
        }
    }

    private void executarLoop(JsonNode params, FrameExecucao frame) {
        JsonNode condicaoNode = params.get("condicao");
        JsonNode etapasInternasNode = params.get("etapas_internas");
        if (etapasInternasNode == null || !etapasInternasNode.isArray()) {
            throw new IllegalArgumentException("Loop precisa de 'etapas_internas' como array");
        }

        List<EtapaProcedimento> etapasInternas = new ArrayList<>();
        for (JsonNode n : etapasInternasNode) {
            EtapaProcedimento etapa = objectMapper.convertValue(n, EtapaProcedimento.class);
            etapasInternas.add(etapa);
        }
        Procedimento bloco = new Procedimento();
        bloco.setEtapas(etapasInternas);
        while (true) {
            ContextoDinamico ctx = criarContextoParaEntidade(estadoSessao.getEntidadeAtual());
            ResultadoExpressao condRes = interpretador.interpretar(condicaoNode, ctx);
            if (!condRes.comoBooleano()) break;

            pilha.push(new FrameExecucao(bloco));
            processarBloco();
            if (estadoSessao.estaSuspenso()) {
                return; 
            }
        }
    }

    private void processarBloco() {
        while (!pilha.isEmpty() && !estadoSessao.estaSuspenso()) {
            FrameExecucao frame = pilha.peek();
            List<EtapaProcedimento> etapas = frame.getProcedimento().getEtapas();
            if (frame.getIndiceEtapaAtual() >= etapas.size()) {
                pilha.pop();
                return; // bloco concluído, retorna ao chamador
            }
            EtapaProcedimento etapa = etapas.get(frame.getIndiceEtapaAtual());
            executarEtapa(etapa, frame);
            if (!estadoSessao.estaSuspenso()) {
                frame.setIndiceEtapaAtual(frame.getIndiceEtapaAtual() + 1);
            }
        }
        // Se suspendeu, salva a pilha
        if (estadoSessao.estaSuspenso()) {
            estadoSessao.setPilhaExecucao(new ArrayDeque<>(pilha));
        }
    }

    private void executarSubprocedimento(JsonNode params, FrameExecucao frame) {
        String idProcedimento = params.get("procedimento_id").asText();
        Procedimento sub = obterProcedimentoPorId(idProcedimento);
        if (sub == null) throw new IllegalArgumentException("Subprocedimento não encontrado: " + idProcedimento);

        pilha.push(new FrameExecucao(sub));
    }

    private Procedimento obterProcedimentoPorId(String id) {
        return null;
    }

    // ---------- HANDLER DE SUSPENSÃO ----------

    private void executarSuspensao(JsonNode params, FrameExecucao frame) {
        String tipo = params.has("tipo") ? params.get("tipo").asText() : "generica";
        String mensagem = params.has("mensagem") ? params.get("mensagem").asText() : "";
        Suspensao suspensao = new Suspensao(tipo, mensagem, params);
        estadoSessao.adicionarSuspensao(suspensao);
    }

    private Map<String, Object> resolverParametros(JsonNode paramsNode, FrameExecucao frame) {
        Map<String, Object> resolvidos = new HashMap<>();
        if (paramsNode != null && paramsNode.isObject()) {
            Iterator<String> campos = paramsNode.fieldNames();
            while (campos.hasNext()) {
                String campo = campos.next();
                JsonNode valorNode = paramsNode.get(campo);
                resolvidos.put(campo, resolverValor(valorNode, frame));
            }
        }
        return resolvidos;
    }

    private Object resolverValor(JsonNode valorNode, FrameExecucao frame) {
        if (valorNode.isObject() && valorNode.has("tipo")) {
            String tipo = valorNode.get("tipo").asText();
            if (tipo.startsWith("alvo")) {
                return valorNode;
            }
            ContextoDinamico ctx = criarContextoParaEntidade(estadoSessao.getEntidadeAtual());
            ResultadoExpressao res = interpretador.interpretar(valorNode, ctx);
            return res.getValor();
        }
        return converterJsonNode(valorNode);
    }


    private ContextoDinamico criarContextoParaEntidade(Long idEntidade) {
        Map<String, AtributoSchema> schemas = extrairSchemasAtributos();
        // O contexto precisa de uma função de interpretação que use ele mesmo.
        // Usamos um array para permitir a auto-referência.
        ContextoDinamico[] holder = new ContextoDinamico[1];
        holder[0] = new ContextoDinamico(
            estadoSessao,
            idEntidade,
            schemas,
            expr -> interpretador.interpretar(expr, holder[0])
        );
        return holder[0];
    }

    private Map<String, AtributoSchema> extrairSchemasAtributos() {
        JsonNode schemaJson = estadoSessao.getSessao().getSistema().getSchemaAtributos();
        Map<String, AtributoSchema> mapa = new HashMap<>();
        Iterator<String> it = schemaJson.fieldNames();
        while (it.hasNext()) {
            String nome = it.next();
            JsonNode node = schemaJson.get(nome);
            AtributoSchema schema = objectMapper.convertValue(node, AtributoSchema.class);
            mapa.put(nome, schema);
        }
        return mapa;
    }

    private Object converterJsonNode(JsonNode node) {
        if (node == null || node.isNull()) return null;
        if (node.isNumber()) return node.numberValue();
        if (node.isTextual()) return node.asText();
        if (node.isBoolean()) return node.booleanValue();
        if (node.isArray()) {
            List<Object> lista = new ArrayList<>();
            for (JsonNode item : node) lista.add(converterJsonNode(item));
            return lista;
        }
        if (node.isObject()) {
            Map<String, Object> mapa = new LinkedHashMap<>();
            Iterator<String> it = node.fieldNames();
            while (it.hasNext()) {
                String key = it.next();
                mapa.put(key, converterJsonNode(node.get(key)));
            }
            return mapa;
        }
        return null;
    }
}