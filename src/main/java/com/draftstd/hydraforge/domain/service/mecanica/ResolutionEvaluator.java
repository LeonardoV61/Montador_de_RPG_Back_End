package com.draftstd.hydraforge.domain.service.mecanica;

import com.draftstd.hydraforge.domain.model.mecanica.Resolucao;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.Map;

@Component
public class ResolutionEvaluator {

    public ResolutionOutcome evaluate(Resolucao resolucao, JsonNode contextoJson) {
        int roll = (int) (Math.random() * 20) + 1;
        int targetValue = 10;
        boolean success = roll >= targetValue;

        return new ResolutionOutcome(
                roll,
                targetValue,
                success,
                "Avaliação de " + resolucao.getNome(),
                Map.of("parametros", resolucao.getParametros() != null ? resolucao.getParametros().toString() : "")
        );
    }
}