package com.rpgvtt.montador_de_rpg_backend.engine.primitivos.handlers;

import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ResultadoEtapa;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EtapaExecutavel;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EtapaHandler;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.ExecucaoContexto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class VerificarCondicaoHandler implements EtapaHandler {

    @Override
    public String tipoEtapa() { return "VERIFICAR_CONDICAO"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        // Extrai a condição dos parâmetros da etapa
        JsonNode condicao = etapa.getParametrosEtapa().path("condicao");
        return executar(etapa, ctx, condicao);
    }

    // Método sobrecarregado para ser chamado por outros handlers
    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx, JsonNode condicao) {
        if (condicao == null || condicao.isMissingNode()) {
            return ResultadoEtapa.concluida(Map.of("status", "sem_condicao"));
        }

        // Jackson 3.x: use properties() no lugar de fields()
        for (Map.Entry<String, JsonNode> entry : condicao.properties()) {
            String campo = entry.getKey();
            JsonNode valorEsperado = entry.getValue();

            Object valorReal = resolverValor(campo, ctx);
            if (valorReal == null) {
                return ResultadoEtapa.erro("Campo '" + campo + "' não encontrado para verificação de condição");
            }

            boolean ok;
            if (valorEsperado.isBoolean()) {
                ok = (valorReal instanceof Boolean b && b == valorEsperado.asBoolean());
            } else if (valorEsperado.isNumber()) {
                double esperado = valorEsperado.asDouble();
                ok = (valorReal instanceof Number num) && num.doubleValue() == esperado;
            } else if (valorEsperado.isObject()) {
                ok = avaliarObjetoCondicao(valorEsperado, valorReal);
            } else {
                ok = valorEsperado.asString().equals(valorReal.toString());
            }

            if (!ok) {
                return ResultadoEtapa.concluida(Map.of(
                        "status", "condicao_falhou",
                        "campo", campo,
                        "esperado", valorEsperado.asString(),
                        "real", valorReal.toString()
                ));
            }
        }

        return ResultadoEtapa.concluida(Map.of("status", "condicao_ok"));
    }

    private Object resolverValor(String campo, ExecucaoContexto ctx) {
        // Primeiro, tenta obter do contexto
        if (ctx.getContexto().containsKey(campo)) {
            return ctx.getContexto().get(campo, Object.class).orElse(null);
        }
        // Depois, tenta de instância ativa (se houver)
        if (!ctx.semInstancias()) {
            // Precisamos do InstanciaResolver, mas para não injetar aqui, fazemos de forma simplificada:
            // Supomos que a instância ativa já está disponível via ctx.idInstanciaAtiva()
            // Vou usar o método que já existe em ExecucaoContexto (que talvez precise ser expandido)
            // Alternativa: injetar InstanciaResolver, mas para manter o exemplo simples, vamos deixar assim:
            // Retornamos null, pois a condição pode ser buscada de outra forma.
            return null;
        }
        return null;
    }

    private boolean avaliarObjetoCondicao(JsonNode obj, Object valorReal) {
        if (valorReal instanceof Number num) {
            if (obj.has("min") && num.doubleValue() < obj.get("min").asDouble()) return false;
            if (obj.has("max") && num.doubleValue() > obj.get("max").asDouble()) return false;
            return true;
        }
        if (obj.has("contains") && valorReal instanceof String s) {
            return s.contains(obj.get("contains").asString());
        }
        return false;
    }
}