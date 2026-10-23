package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.primitivos.HandlerRegistry;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EscopoInstancias;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// In DB:
// {
//   "fonte":           "contexto.ids_inimigos",
//   "sub_etapa_tipo":  "VERIFICAR_CONDICAO",
//   "sub_etapa_params": { "atributo": "hp", "operador": "MAIOR_QUE", "valor": 0 },
//   "salvar_ids_em":   "ids_inimigos_vivos",  // IDs that passed the check
//   "salvar_count_em": "count_inimigos_vivos"
// }
@Component
public class ForEachHandler implements EtapaHandler {

    private final InstanciaResolver instanciaResolver;
    private final HandlerRegistry handlers;

    @Override
    public String tipoEtapa() { return "PARA_CADA"; }

    public ForEachHandler(InstanciaResolver instanciaResolver,
                          @Lazy HandlerRegistry handlerRegistry) {
        this.instanciaResolver = instanciaResolver;
        this.handlers = handlerRegistry;
    }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        String fonte          = (String) params.get("fonte");
        String subTipo        = (String) params.get("sub_etapa_tipo");
        String salvarIdsEm    = (String) params.get("salvar_ids_em");
        String salvarCountEm  = (String) params.get("salvar_count_em");

        @SuppressWarnings("unchecked")
        Map<String, Object> subParams = (Map<String, Object>)
                params.getOrDefault("sub_etapa_params", Map.of());

        List<EntidadeInstancia> instancias = instanciaResolver.resolverDeFonte(fonte, ctx);

        EtapaHandler subHandler = handlers.get(subTipo);
        if (subHandler == null) {
            throw new IllegalArgumentException("Sub-handler não encontrado: " + subTipo);
        }

        // Constrói uma sub-etapa utilizando o construtor do modelo imutável
        EtapaProcedimento subEtapa = new EtapaProcedimento(
                UUID.randomUUID(), null, 1, "SubEtapaForEach", subTipo, subParams, true
        );

        List<UUID> idsPasaram = new ArrayList<>();
        Map<UUID, Object> resultadosPorId = new LinkedHashMap<>();

        for (EntidadeInstancia instancia : instancias) {
            // Define temporariamente o escopo para esta única instância durante a execução do sub-handler
            EscopoInstancias escopoOriginal = ctx.getEscopo();
            ctx.setEscopo(EscopoInstancias.unica(instancia.getId()));

            ResultadoEtapa resultado = subHandler.executar(subEtapa, ctx);

            ctx.setEscopo(escopoOriginal); // restaura o escopo original

            resultadosPorId.put(instancia.getId(), resultado.dados());

            if (resultado.tipo() == ResultadoEtapa.Tipo.CONCLUIDA) {
                idsPasaram.add(instancia.getId());
            }
        }

        // Salva os resultados no contexto para utilização por handlers subsequentes
        if (salvarIdsEm != null) {
            ctx.getContexto().put(salvarIdsEm, idsPasaram);
        }
        if (salvarCountEm != null) {
            ctx.getContexto().put(salvarCountEm, (long) idsPasaram.size());
        }

        return ResultadoEtapa.concluida(Map.of(
                "total",         instancias.size(),
                "passaram",      idsPasaram.size(),
                "resultados",    resultadosPorId
        ));
    }
}