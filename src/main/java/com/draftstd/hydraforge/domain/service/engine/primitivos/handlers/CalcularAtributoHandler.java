package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.components.InterpretadorJson;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InterpretadorContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;
import java.util.UUID;

@Component
public class CalcularAtributoHandler implements EtapaHandler {

    private final InterpretadorJson interpretadorJson;
    private final InstanciaResolver instanciaResolver;
    private final EntidadeInstanciaOutputPort entidadeInstanciaPort;
    private final CenaPersistencePort cenaPort;
    private final JsonMapper mapper;

    public CalcularAtributoHandler(InterpretadorJson interpretadorJson,
                                  InstanciaResolver instanciaResolver,
                                  EntidadeInstanciaOutputPort entidadeInstanciaPort,
                                  CenaPersistencePort cenaPort,
                                  JsonMapper mapper) {
        this.interpretadorJson = interpretadorJson;
        this.instanciaResolver = instanciaResolver;
        this.entidadeInstanciaPort = entidadeInstanciaPort;
        this.cenaPort = cenaPort;
        this.mapper = mapper;
    }

    @Override
    public String tipoEtapa() { return "CALCULAR_ATRIBUTO"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        String ctxKey = exigirTexto(params, "salvar_em", etapa);
        Object rawExpressao = params.get("expressao");
        if (rawExpressao == null) throw new JsonFieldNotFoundException("expressao", etapa.getNome());

        JsonNode expressaoNode = rawExpressao instanceof JsonNode node ? node : mapper.valueToTree(rawExpressao);

        // Se houver source_key opcional, injeta previamente no contexto antes da interpretação
        if (params.containsKey("source_key")) {
            String sourceKey = (String) params.get("source_key");
            Integer anterior = ctx.getContexto().getInt(sourceKey)
                    .orElseThrow(() -> new JsonFieldNotFoundException("source_key", etapa.getNome()));
            ctx.getContexto().put("resultado", anterior);
        }

        InterpretadorContexto intCtx = new InterpretadorContexto(
                (ProcedimentoContexto) ctx, instanciaResolver, cenaPort, mapper);

        double val = interpretadorJson.interpretar(expressaoNode, intCtx).comoNumero();

        ctx.getContexto().put(ctxKey, val);

        return ResultadoEtapa.concluida(Map.of("salvoEm", ctxKey, "valor", val));
    }

    private EntidadeInstancia resolverInstancia(Map<String, Object> params, ExecucaoContexto ctx) {
        Object idObj = params.get("id_entidade");
        if (idObj == null) {
            return instanciaResolver.retornarAtiva(ctx);
        }
        UUID idEntidade = idObj instanceof UUID u ? u : UUID.fromString(idObj.toString());
        return entidadeInstanciaPort.buscarPorId(idEntidade)
                .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, idEntidade));
    }

    private String exigirTexto(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object val = params.get(chave);
        if (val == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return val.toString();
    }
}