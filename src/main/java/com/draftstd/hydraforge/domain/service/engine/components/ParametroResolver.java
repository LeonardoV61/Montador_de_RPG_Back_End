package com.draftstd.hydraforge.domain.service.engine.components;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaPort;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import tools.jackson.databind.JsonNode;

public class ParametroResolver {

    private final InstanciaResolver instanciaResolver;
    private final ItemResolver itemResolver;
    private final EntidadeInstanciaPort instanciaPort;

    public ParametroResolver(InstanciaResolver instanciaResolver, ItemResolver itemResolver, EntidadeInstanciaPort instanciaPort) {
        this.instanciaResolver = instanciaResolver;
        this.itemResolver = itemResolver;
        this.instanciaPort = instanciaPort;
    }

    public double resolverNumero(JsonNode origem, ExecucaoContexto ctx) {
        if (origem == null || origem.isMissingNode() || origem.isNull()) return 0;
        String fonte = origem.path("fonte").asString("ausente");

        return switch (fonte) {
            case "ausente" -> 0;
            case "fixo"     -> origem.path("chave").asDouble(0);
            case "contexto" -> ctx.getContexto()
                    .getInt(origem.path("chave").asString())
                    .map(Integer::doubleValue)
                    .orElse(0.0);
            case "atributo" -> resolverInstancia(origem, ctx)
                    .getAtributosAtuais()
                    .path(origem.path("chave").asString())
                    .asDouble(0);
            case "item_equipado" -> {
                Long idInst = origem.has("id_entidade")
                        ? origem.get("id_entidade").asLong()
                        : ctx.idInstanciaAtiva();
                JsonNode val = itemResolver.atributoDoEquipado(
                        idInst, origem.path("slot").asString(), origem.path("chave").asString());
                yield val != null ? val.asDouble(0) : 0;
            }
            default -> throw new IllegalArgumentException("fonte desconhecida: " + fonte);
        };
    }

    public String resolverTexto(JsonNode origem, ExecucaoContexto ctx) {
        if (origem == null || origem.isMissingNode() || origem.isNull()) return null;

        if (origem.isString()) return origem.asString();

        String fonte = origem.path("fonte").asString("ausente");
        return switch (fonte) {
            case "ausente" -> null;
            case "fixo"     -> origem.path("chave").asString();
            case "atributo" -> resolverInstancia(origem, ctx)
                    .getAtributosAtuais().path(origem.path("chave").asString()).asString(null);
            case "item_equipado" -> {
                Long idInst = origem.has("id_entidade") ? origem.get("id_entidade").asLong() : ctx.idInstanciaAtiva();
                JsonNode val = itemResolver.atributoDoEquipado(
                        idInst, origem.path("slot").asString(), origem.path("chave").asString());
                yield val != null ? val.asString(null) : null;
            }
            default -> throw new IllegalArgumentException("fonte de texto desconhecida: " + fonte);
        };
    }

    public int resolverSomaAtributos(JsonNode listaAtributos, ExecucaoContexto ctx) {
        if (listaAtributos == null || !listaAtributos.isArray() || ctx.semInstancias()) return 0;
        JsonNode attrs = instanciaResolver.retornarAtiva(ctx).getAtributosAtuais();
        int soma = 0;
        for (JsonNode n : listaAtributos) {
            JsonNode v = attrs.get(n.asString());
            if (v != null && v.isNumber()) soma += v.asInt();
        }
        return soma;
    }

    private EntidadeInstancia resolverInstancia(JsonNode origem, ExecucaoContexto ctx) {
        if (origem.has("id_entidade")) {
            Long id = origem.get("id_entidade").asLong();
            return instanciaPort.buscarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, id));
        }
        return instanciaResolver.retornarAtiva(ctx);
    }
}