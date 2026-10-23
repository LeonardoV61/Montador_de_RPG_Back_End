package com.draftstd.hydraforge.domain.service.engine.components;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import tools.jackson.databind.JsonNode;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Component
public class ParametroResolver {

    private final InstanciaResolver instanciaResolver;
    private final ItemResolver itemResolver;
    private final EntidadeInstanciaOutputPort instanciaPort;

    public ParametroResolver(InstanciaResolver instanciaResolver, ItemResolver itemResolver, EntidadeInstanciaOutputPort instanciaPort) {
        this.instanciaResolver = instanciaResolver;
        this.itemResolver = itemResolver;
        this.instanciaPort = instanciaPort;
    }

    public double resolverNumero(JsonNode origem, ExecucaoContexto ctx) {
        if (origem == null || origem.isMissingNode() || origem.isNull()) return 0;
        String fonte = origem.path("fonte").asText("ausente");

        return switch (fonte) {
            case "ausente" -> 0;
            case "fixo"     -> origem.path("chave").asDouble(0);
            case "contexto" -> ctx.getContexto()
                    .getInt(origem.path("chave").asText())
                    .map(Integer::doubleValue)
                    .orElse(0.0);
            case "atributo" -> {
                Map<String, Object> attrs = resolverInstancia(origem, ctx).getValoresAtributos();
                Object val = attrs != null ? attrs.get(origem.path("chave").asText()) : null;
                yield val instanceof Number n ? n.doubleValue() : (val != null ? Double.parseDouble(val.toString()) : 0.0);
            }
            case "item_equipado" -> {
                UUID idInst = origem.has("id_entidade")
                        ? UUID.fromString(origem.get("id_entidade").asText())
                        : ctx.idInstanciaAtiva();
                Object val = itemResolver.atributoDoEquipado(
                        idInst, origem.path("slot").asText(), origem.path("chave").asText());
                if (val instanceof Number n) yield n.doubleValue();
                if (val != null) {
                    try {
                        yield Double.parseDouble(val.toString());
                    } catch (NumberFormatException ignored) {}
                }
                yield 0.0;
            }
            default -> throw new IllegalArgumentException("fonte desconhecida: " + fonte);
        };
    }

    public String resolverTexto(JsonNode origem, ExecucaoContexto ctx) {
        if (origem == null || origem.isMissingNode() || origem.isNull()) return null;

        if (origem.isString()) return origem.asText();

        String fonte = origem.path("fonte").asText("ausente");
        return switch (fonte) {
            case "ausente" -> null;
            case "fixo"     -> origem.path("chave").asText();
            case "atributo" -> {
                Map<String, Object> attrs = resolverInstancia(origem, ctx).getValoresAtributos();
                Object val = attrs != null ? attrs.get(origem.path("chave").asText()) : null;
                yield val != null ? val.toString() : null;
            }
            case "item_equipado" -> {
                UUID idInst = origem.has("id_entidade") 
                        ? UUID.fromString(origem.get("id_entidade").asText()) 
                        : ctx.idInstanciaAtiva();
                Object val = itemResolver.atributoDoEquipado(
                        idInst, origem.path("slot").asText(), origem.path("chave").asText());
                yield val != null ? val.toString() : null;
            }
            default -> throw new IllegalArgumentException("fonte de texto desconhecida: " + fonte);
        };
    }

    /**
     * Resolve a soma dos atributos indicados na lista, suportando tanto
     * objetos do tipo Collection (ex: List<String>) quanto JsonNode.
     */
    public int resolverSomaAtributos(Object listaAtributos, ExecucaoContexto ctx) {
        if (listaAtributos == null || ctx.semInstancias()) return 0;

        EntidadeInstancia ativa = instanciaResolver.retornarAtiva(ctx);
        Map<String, Object> attrs = ativa.getValoresAtributos();
        if (attrs == null || attrs.isEmpty()) return 0;

        int soma = 0;

        if (listaAtributos instanceof Collection<?> colecao) {
            for (Object item : colecao) {
                if (item != null) {
                    Object val = attrs.get(item.toString());
                    if (val instanceof Number n) {
                        soma += n.intValue();
                    } else if (val != null) {
                        try {
                            soma += Integer.parseInt(val.toString());
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
        } else if (listaAtributos instanceof JsonNode node && node.isArray()) {
            for (JsonNode n : node) {
                Object val = attrs.get(n.asText());
                if (val instanceof Number num) {
                    soma += num.intValue();
                } else if (val != null) {
                    try {
                        soma += Integer.parseInt(val.toString());
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        return soma;
    }

    private EntidadeInstancia resolverInstancia(JsonNode origem, ExecucaoContexto ctx) {
        if (origem.has("id_entidade")) {
            UUID id = UUID.fromString(origem.get("id_entidade").asText());
            return instanciaPort.buscarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, id));
        }
        return instanciaResolver.retornarAtiva(ctx);
    }
}