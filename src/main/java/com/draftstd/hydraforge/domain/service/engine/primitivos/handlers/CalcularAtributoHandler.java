package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.components.InterpretadorJson;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto.ContextoJsonNode;
import com.draftstd.hydraforge.adapters.out.repository.entidade.EntidadeInstanciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CalcularAtributoHandler implements EtapaHandler {

    private final InterpretadorJson interpretadorJson;
    private final JsonMapper mapper;
    private final InstanciaResolver instanciaResolver;
    private final EntidadeInstanciaRepository instanciaRepo;

    @Override
    public String tipoEtapa() { return "CALCULAR_ATRIBUTO"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        JsonNode params = etapa.getParametrosEtapa();

        String ctxKey = exigirTexto(params, "salvar_em", etapa);
        JsonNode expressao = params.get("expressao"); // lido direto do JsonNode — nunca via Map
        if (expressao == null) throw new JsonFieldNotFoundException("expressao", etapa.getNome());

        EntidadeInstancia inst = resolverInstancia(params, ctx);

        ObjectNode execCtx = mapper.createObjectNode();
        execCtx.set("atributos", inst.getAtributosAtuais());

        // source_key agora é opcional — fórmulas puras não precisam de um valor prévio
        if (params.has("source_key")) {
            String sourceKey = params.get("source_key").asString();
            Integer anterior = ctx.getContexto().getInt(sourceKey)
                    .orElseThrow(() -> new JsonFieldNotFoundException("source_key", etapa.getNome()));
            execCtx.put("resultado", anterior);
        }

        ContextoJsonNode ctxFinal = new ContextoJsonNode(execCtx);
        double val = interpretadorJson.interpretar(expressao, ctxFinal).comoNumero();

        ctx.getContexto().put(ctxKey, val);

        return ResultadoEtapa.concluida(Map.of("salvoEm", ctxKey, "valor", val));
    }

    private EntidadeInstancia resolverInstancia(JsonNode params, ExecucaoContexto ctx) {
        JsonNode idNode = params.path("id_entidade");
        if (idNode.isMissingNode() || idNode.isNull()) {
            return instanciaResolver.retornarAtiva(ctx);
        }
        UUID idEntidade = UUID.fromString(idNode.asString());
        return instanciaRepo.findById(idEntidade)
                .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, idEntidade));
    }

    private String exigirTexto(JsonNode params, String chave, EtapaExecutavel etapa) {
        JsonNode v = params.path(chave);
        if (v.isMissingNode() || v.isNull()) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return v.asString();
    }
}