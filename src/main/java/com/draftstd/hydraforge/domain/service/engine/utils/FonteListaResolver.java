package com.draftstd.hydraforge.domain.service.engine.utils;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Resolve uma lista de IDs (UUID) a partir de uma fonte padronizada.
 */
@Component
@RequiredArgsConstructor
public class FonteListaResolver {

    private final InstanciaResolver instanciaResolver;

    public List<UUID> resolverIds(String fonte, JsonNode literalNode, ExecucaoContexto ctx,
                                  boolean filtroVivo, String atributoVida) {
        List<UUID> ids = resolverIdsBruto(fonte, literalNode, ctx);
        if (!filtroVivo) return ids;

        return ids.stream()
                .filter(Objects::nonNull)
                .map(instanciaResolver::buscarPorId)
                .filter(Objects::nonNull)
                .filter(i -> i.getAtributosAtuais() != null && 
                             i.getAtributosAtuais().path(atributoVida).asInt(0) > 0)
                .map(EntidadeInstancia::getId)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<String> resolverIdsComoTexto(String fonte, JsonNode literalNode, ExecucaoContexto ctx,
                                             boolean filtroVivo, String atributoVida) {
        return resolverIds(fonte, literalNode, ctx, filtroVivo, atributoVida)
                .stream()
                .filter(Objects::nonNull)
                .map(UUID::toString)
                .toList();
    }

    private List<UUID> resolverIdsBruto(String fonte, JsonNode literalNode, ExecucaoContexto ctx) {
        if ("estatico".equals(fonte))            return paraListaUuid(literalNode);
        if (fonte.startsWith("contexto."))       return ctx.getContexto()
                .getListaIds(fonte.substring("contexto.".length())) // Se getListaIds retornar strings/UUIDs, convertemos abaixo
                .stream()
                .map(this::converterParaUuidSeguro)
                .filter(Objects::nonNull)
                .toList();
        if (fonte.startsWith("config_sistema.")) return paraListaUuid(
                ctx.getSistema().getConfiguracao().path(fonte.substring("config_sistema.".length())));

        // instâncias ativas, participantes de cena, etc.
        return instanciaResolver.resolverDeFonte(fonte, ctx).stream()
                .filter(Objects::nonNull)
                .map(EntidadeInstancia::getId)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<UUID> paraListaUuid(JsonNode arr) {
        List<UUID> out = new ArrayList<>();
        if (arr != null && arr.isArray()) {
            arr.forEach(n -> {
                UUID uuid = converterParaUuidSeguro(n.asText());
                if (uuid != null) out.add(uuid);
            });
        }
        return out;
    }

    private UUID converterParaUuidSeguro(Object obj) {
        if (obj == null) return null;
        try {
            if (obj instanceof UUID u) return u;
            String str = obj.toString();
            if (!str.isBlank()) {
                return UUID.fromString(str);
            }
        } catch (IllegalArgumentException ignored) {
            // Caso o formato não seja um UUID válido
        }
        return null;
    }
}