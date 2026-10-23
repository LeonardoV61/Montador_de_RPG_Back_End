package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class DefinirAlvoHandler implements EtapaHandler {

    @Override
    public String tipoEtapa() { return "DEFINIR_ALVO"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();
        String salvarEm = exigirTexto(params, "salvar_em", etapa);
        String prompt = (String) params.getOrDefault("prompt", "Selecione um alvo no mapa");

        // Se o contexto já tem a variável, significa que o front-end já respondeu
        if (ctx.getContexto().containsKey(salvarEm)) {
            Object rawAlvo = ctx.getContexto().get(salvarEm, Object.class)
                    .orElseThrow(() -> new JsonFieldNotFoundException(salvarEm, etapa.getNome()));
            
            UUID alvoId = rawAlvo instanceof UUID u ? u : UUID.fromString(rawAlvo.toString());
            return ResultadoEtapa.concluida(Map.of("alvo_definido", alvoId));
        }

        // Pausa a execução e envia o payload pedindo interação no mapa
        return ResultadoEtapa.aguardandoInput(Map.of(
            "tipo_input", "SELECAO_TOKEN",
            "campoPedido", prompt,
            "salvar_em", salvarEm
        ));
    }

    private String exigirTexto(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object val = params.get(chave);
        if (val == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return val.toString();
    }
}