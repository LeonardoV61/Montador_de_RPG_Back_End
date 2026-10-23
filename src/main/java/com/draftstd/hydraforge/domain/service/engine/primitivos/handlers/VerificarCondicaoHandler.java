package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.components.InterpretadorJson;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InterpretadorContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.ResultadoExpressao;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class VerificarCondicaoHandler implements EtapaHandler {

    private final InstanciaResolver instanciaResolver;
    private final InterpretadorJson interpretador;
    private final CenaPersistencePort cenaPort;
    private final JsonMapper mapper;

    @Override
    public String tipoEtapa() { return "VERIFICAR_CONDICAO"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();
        Object condicaoObj = params.get("condicao");
        
        JsonNode condicaoNode = condicaoObj != null ? mapper.valueToTree(condicaoObj) : null;
        return verificar(condicaoNode, ctx);
    }

    public ResultadoEtapa verificar(JsonNode condicaoNode, ExecucaoContexto ctx) {
        if (condicaoNode == null || condicaoNode.isMissingNode() || condicaoNode.isNull()) {
            return ResultadoEtapa.concluida(Map.of("status", "sem_condicao"));
        }

        // Se for uma expressão do InterpretadorJson (objeto com "tipo"), delega
        if (condicaoNode.isObject() && condicaoNode.has("tipo")) {
            InterpretadorContexto intCtx = new InterpretadorContexto(
                (ProcedimentoContexto) ctx, instanciaResolver, cenaPort, mapper);
            ResultadoExpressao resultado = interpretador.interpretar(condicaoNode, intCtx);
            boolean ok = resultado.comoBooleano();
            if (!ok) {
                return ResultadoEtapa.concluida(Map.of("status", "condicao_falhou"));
            }
            return ResultadoEtapa.concluida(Map.of("status", "condicao_ok"));
        }

        return verificarLegado(condicaoNode, ctx);
    }

    private ResultadoEtapa verificarLegado(JsonNode condicao, ExecucaoContexto ctx) {
        if (condicao == null || condicao.isMissingNode() || condicao.isNull()) {
            return ResultadoEtapa.concluida(Map.of("status", "sem_condicao"));
        }

        for (Map.Entry<String, JsonNode> entry : condicao.properties()) {
            String campo = entry.getKey();
            JsonNode valorEsperado = entry.getValue();

            Object valorReal = resolverValor(campo, ctx);
            if (valorReal == null) {
                return ResultadoEtapa.erro("Campo '" + campo + "' não encontrado");
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
                ok = valorEsperado.asText().equals(valorReal.toString());
            }

            if (!ok) {
                return ResultadoEtapa.concluida(Map.of(
                        "status", "condicao_falhou",
                        "campo", campo,
                        "esperado", valorEsperado.asText(),
                        "real", valorReal.toString()
                ));
            }
        }

        return ResultadoEtapa.concluida(Map.of("status", "condicao_ok"));
    }

    private Object resolverValor(String campo, ExecucaoContexto ctx) {
        // Tenta obter do contexto
        if (ctx.getContexto().containsKey(campo)) {
            return ctx.getContexto().get(campo, Object.class).orElse(null);
        }
        // Tenta obter da instância ativa via mapa de atributos do novo modelo de domínio
        if (!ctx.semInstancias()) {
            try {
                EntidadeInstancia instancia = instanciaResolver.retornarAtiva(ctx);
                Map<String, Object> atributos = instancia.getValoresAtributos();
                if (atributos != null && atributos.containsKey(campo)) {
                    return atributos.get(campo);
                }
            } catch (Exception e) {
                // Instância não encontrada ou sem o atributo
            }
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
            return s.contains(obj.get("contains").asText());
        }
        return false;
    }
}