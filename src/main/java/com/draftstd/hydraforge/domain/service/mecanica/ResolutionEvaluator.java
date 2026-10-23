package com.draftstd.hydraforge.domain.service.mecanica;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.model.mecanica.Resolucao;

import java.util.Map;

@Component
public class ResolutionEvaluator {

    public ResolutionOutcome evaluate(Resolucao resolucao, Map<String, Object> contexto) {
        int roll = (int) (Math.random() * 20) + 1;
        int targetValue = 10;
        boolean success = roll >= targetValue;

        String parametrosString = (resolucao != null && resolucao.getParametrosJson() != null) 
                ? resolucao.getParametrosJson() 
                : "";

        String nomeResolucao = (resolucao != null) ? resolucao.getNome() : "Desconhecida";

        return new ResolutionOutcome(
                roll,
                targetValue,
                success,
                "Avaliação de " + nomeResolucao,
                Map.of("parametros", parametrosString)
        );
    }
}