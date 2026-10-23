package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SolicitarInputHandler implements EtapaHandler {

    private final InstanciaResolver instanciaResolver;
    private final JsonMapper mapper;

    @Override
    public String tipoEtapa() { return "SOLICITAR_INPUT"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        String ctxKey = exigirCampo(params, "salvar_em", etapa);
        String campoPedido = exigirCampo(params, "campo_pedido", etapa);

        Object rawPodePassar = params.get("pode_passar");
        boolean podePassar = rawPodePassar instanceof Boolean b ? b
                : (rawPodePassar == null || Boolean.parseBoolean(rawPodePassar.toString()));

        Object rawRecurso = params.get("recurso_necessario");
        String recursoNecessario = rawRecurso != null ? rawRecurso.toString() : null;

        if (recursoNecessario != null && !ctx.semInstancias()) {
            EntidadeInstancia inst = instanciaResolver.retornarAtiva(ctx);
            Map<String, Object> attrs = inst.getValoresAtributos();
            Object val = attrs != null ? attrs.get(recursoNecessario) : null;

            boolean disponivel = false;
            if (val instanceof Boolean b) {
                disponivel = b;
            } else if (val instanceof Number n) {
                disponivel = n.longValue() > 0;
            } else if (val != null) {
                disponivel = Boolean.parseBoolean(val.toString());
            }

            if (!disponivel) {
                return ResultadoEtapa.concluida(Map.of(
                        "campoPedido", campoPedido, "status", "recurso_indisponivel"));
            }
        }

        if (!ctx.getContexto().containsKey(ctxKey)) {
            return ResultadoEtapa.aguardandoInput(Map.of(
                    "campoPedido",  campoPedido,
                    "opcoes",       resolverOpcoes(params, ctx),
                    "pode_passar",  podePassar,
                    "salvar_em",    ctxKey
            ));
        }

        Object raw = ctx.getContexto().get(ctxKey, Object.class).orElse(null);
        String escolha = raw != null ? raw.toString() : null;

        if ("PASSAR".equals(escolha) && podePassar) {
            return ResultadoEtapa.concluida(Map.of("campoPedido", campoPedido, "status", "passou"));
        }

        return ResultadoEtapa.concluida(Map.of("campoPedido", campoPedido, "escolha", escolha));
    }

    private String exigirCampo(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object val = params.get(chave);
        if (val == null) {
            throw new JsonFieldNotFoundException(chave, etapa.getNome());
        }
        return val.toString();
    }

    private List<String> resolverOpcoes(Map<String, Object> params, ExecucaoContexto ctx) {
        Object rawFonte = params.get("opcoes_fonte");
        String fonte = rawFonte != null ? rawFonte.toString() : "";

        if ("estatico".equals(fonte)) {
            return colecaoParaLista(params.get("opcoes_estatico"));
        }

        if (fonte.startsWith("config_sistema.")) {
            String chave = fonte.substring("config_sistema.".length());
            Map<String, Object> configMap = ctx.getSistema().getConfiguracao();
            Object raw = configMap != null ? configMap.get(chave) : null;
            if (raw == null) {
                log.warn("config_sistema.{} não encontrado no sistema {}", chave, ctx.getIdSistema());
                return List.of();
            }
            return colecaoParaLista(raw);
        }

        if (fonte.startsWith("contexto.")) {
            String chave = fonte.substring("contexto.".length());
            return ctx.getContexto().get(chave, List.class).orElseGet(() -> {
                log.warn("Chave '{}' não encontrada no contexto do procedimento", chave);
                return List.of();
            });
        }

        if (fonte.startsWith("atributos_instancia.")) {
            String chave = fonte.substring("atributos_instancia.".length());
            if (ctx.semInstancias()) {
                log.warn("opcoes_fonte '{}' requer instância mas escopo é NENHUMA", fonte);
                return List.of();
            }
            EntidadeInstancia inst = instanciaResolver.retornarAtiva(ctx);
            Map<String, Object> attrs = inst.getValoresAtributos();
            Object rawAttr = attrs != null ? attrs.get(chave) : null;
            return colecaoParaLista(rawAttr);
        }

        log.warn("opcoes_fonte desconhecido: '{}'", fonte);
        return List.of();
    }

    private List<String> colecaoParaLista(Object obj) {
        if (obj == null) return List.of();

        if (obj instanceof Collection<?> colecao) {
            List<String> out = new ArrayList<>();
            for (Object item : colecao) {
                if (item instanceof Map<?, ?> map) {
                    Object valor = map.get("valor");
                    Object label = map.get("label");
                    out.add(valor != null ? valor.toString() : (label != null ? label.toString() : map.toString()));
                } else if (item != null) {
                    out.add(item.toString());
                }
            }
            return out;
        }

        JsonNode node = mapper.valueToTree(obj);
        if (node.isArray()) {
            List<String> out = new ArrayList<>();
            node.forEach(n -> {
                if (n.isObject()) {
                    String valor = n.has("valor") ? n.get("valor").asText() : n.path("label").asText(null);
                    out.add(valor != null ? valor : n.toString());
                } else {
                    out.add(n.asText());
                }
            });
            return out;
        }

        return List.of(obj.toString());
    }
}