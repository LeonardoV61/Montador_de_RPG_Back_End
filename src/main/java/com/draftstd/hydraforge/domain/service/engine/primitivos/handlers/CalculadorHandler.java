package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CalculadorHandler implements EtapaHandler {

    @Override
    public String tipoEtapa() { return "CALCULAR"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        // Correção: getParametrosEtapa() em vez de getParametrosComoMapa()
        Map<String, Object> params = etapa.getParametrosEtapa();
        
        String listaOrigem = (String) params.get("lista");
        String operacao = (String) params.get("operacao");
        String salvarEm = (String) params.get("salvar_em");

        List<Object> lista = ctx.getContexto().getList(listaOrigem);
        
        if (lista.isEmpty()) {
            throw new IllegalArgumentException("Lista vazia para cálculo: " + listaOrigem);
        }

        List<Double> numeros = lista.stream()
                .map(obj -> Double.valueOf(obj.toString()))
                .collect(Collectors.toList());

        double resultado = switch (operacao.toUpperCase()) {
            case "MAX" -> numeros.stream().mapToDouble(v -> v).max().orElse(0);
            case "MIN" -> numeros.stream().mapToDouble(v -> v).min().orElse(0);
            case "SUM" -> numeros.stream().mapToDouble(v -> v).sum();
            case "AVG" -> numeros.stream().mapToDouble(v -> v).average().orElse(0);
            default -> throw new IllegalArgumentException("Operação não suportada: " + operacao);
        };

        ctx.getContexto().put(salvarEm, resultado);

        return ResultadoEtapa.concluida(Map.of(
            "operacao", operacao,
            "resultado", resultado
        ));
    }
}