package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.service.engine.components.ParametroResolver;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.engine.utils.rolagemEngine.ResultadoPool;
import com.draftstd.hydraforge.domain.service.engine.utils.rolagemEngine.RolagemEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RolarPoolHandler implements EtapaHandler {
    /**
     * parametros_etapa:
     * {
     *   "tamanho_fonte": "atributos_somados" | "fixo" | "contexto",
     *   "atributos": ["Forca", "Briga"],     -- quando atributos_somados
     *   "valor": 5,                          -- quando fixo
     *   "chave_contexto": "pool_tamanho",     -- quando contexto
     *
     *   "dado": "d10", "dificuldade": 6, "conta_uns": true,
     *   "critico_em": "dois_maximos_consecutivos" | "qualquer_maximo" | null,
     *   "salvar_em": "resultado_ataque"
     * }
     */
    private final RolagemEngine rolagemEngine;
    private final ParametroResolver resolver;

    @Override
    public String tipoEtapa() { return "ROLAR_POOL"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        String dado = exigirTexto(params, "dado", etapa);
        
        Object rawContaUns = params.get("conta_uns");
        boolean contaUns = rawContaUns instanceof Boolean b ? b 
                : (rawContaUns != null && Boolean.parseBoolean(rawContaUns.toString()));

        Object rawCriticoEm = params.get("critico_em");
        String criticoEm = rawCriticoEm != null ? rawCriticoEm.toString() : null;

        Object rawDificuldade = params.get("dificuldade");
        Integer dificuldade = rawDificuldade instanceof Number n ? n.intValue() 
                : (rawDificuldade != null ? Integer.parseInt(rawDificuldade.toString()) : null);

        Object rawSalvarEm = params.get("salvar_em");
        String salvarEm = rawSalvarEm != null ? rawSalvarEm.toString() : null;

        int tamanhoPool = resolverTamanho(params, ctx, etapa);

        if (tamanhoPool <= 0) {
            if (salvarEm != null) ctx.getContexto().put(salvarEm, 0);
            return ResultadoEtapa.concluida(Map.of(
                    "tamanhoPool", 0, "rolos", List.of(), "sucessos", 0, "motivo", "pool vazio"));
        }

        ResultadoPool r = rolagemEngine.executarPool(tamanhoPool, dado, dificuldade, contaUns, criticoEm);

        if (salvarEm != null) {
            ctx.getContexto().put(salvarEm, r.sucessosLiquidos());
            ctx.getContexto().put(salvarEm + "_rolos", r.rolos());
            ctx.getContexto().put(salvarEm + "_critico", r.critico());
            ctx.getContexto().put(salvarEm + "_botch", r.botch());
        }

        return ResultadoEtapa.concluida(Map.of(
                "tamanhoPool", tamanhoPool, "dado", dado,
                "dificuldade", dificuldade != null ? dificuldade : "nenhuma",
                "rolos", r.rolos(), "sucessos", r.sucessosLiquidos(), "falhas", r.falhas(),
                "critico", r.critico(), "botch", r.botch()
        ));
    }

    private int resolverTamanho(Map<String, Object> params, ExecucaoContexto ctx, EtapaExecutavel etapa) {
        Object rawFonte = params.get("tamanho_fonte");
        String fonte = rawFonte != null ? rawFonte.toString() : "atributos_somados";

        return switch (fonte) {
            case "atributos_somados" -> resolver.resolverSomaAtributos(params.get("atributos"), ctx);
            case "fixo" -> {
                Object val = params.get("valor");
                yield val instanceof Number n ? n.intValue() 
                        : (val != null ? Integer.parseInt(val.toString()) : 0);
            }
            case "contexto" -> ctx.getContexto()
                    .getInt(exigirTexto(params, "chave_contexto", etapa)).orElse(0);
            default -> throw new IllegalArgumentException("tamanho_fonte desconhecida: " + fonte);
        };
    }

    private String exigirTexto(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object val = params.get(chave);
        if (val == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return val.toString();
    }
}