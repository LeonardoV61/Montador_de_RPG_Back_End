package com.draftstd.hydraforge.domain.service.engine.utils;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Resolve uma lista de IDs (UUID) a partir de uma fonte padronizada.
 */
@Component
@RequiredArgsConstructor
public class FonteListaResolver {

    private final InstanciaResolver instanciaResolver;

    public List<UUID> resolverIds(String fonte, Object valorLiteral, ExecucaoContexto ctx,
                                  boolean filtroVivo, String atributoVida) {
        List<UUID> ids = resolverIdsBruto(fonte, valorLiteral, ctx);
        if (!filtroVivo) return ids;

        return ids.stream()
                .filter(Objects::nonNull)
                .map(instanciaResolver::buscarPorId)
                .filter(Objects::nonNull)
                .filter(i -> {
                    Map<String, Object> attrs = i.getValoresAtributos();
                    if (attrs == null || !attrs.containsKey(atributoVida)) return false;
                    Object val = attrs.get(atributoVida);
                    if (val instanceof Number n) return n.doubleValue() > 0;
                    try {
                        return Double.parseDouble(val.toString()) > 0;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(EntidadeInstancia::getId)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<String> resolverIdsComoTexto(String fonte, Object valorLiteral, ExecucaoContexto ctx,
                                             boolean filtroVivo, String atributoVida) {
        return resolverIds(fonte, valorLiteral, ctx, filtroVivo, atributoVida)
                .stream()
                .filter(Objects::nonNull)
                .map(UUID::toString)
                .toList();
    }

    private List<UUID> resolverIdsBruto(String fonte, Object valorLiteral, ExecucaoContexto ctx) {
        if ("estatico".equals(fonte)) {
            return paraListaUuid(valorLiteral);
        }
        
        if (fonte.startsWith("contexto.")) {
            return ctx.getContexto()
                    .getListaIds(fonte.substring("contexto.".length()))
                    .stream()
                    .map(this::converterParaUuidSeguro)
                    .filter(Objects::nonNull)
                    .toList();
        }
        
        if (fonte.startsWith("config_sistema.")) {
            Map<String, Object> config = ctx.getSistema() != null ? ctx.getSistema().getConfiguracao() : null;
            Object val = config != null ? config.get(fonte.substring("config_sistema.".length())) : null;
            return paraListaUuid(val);
        }

        return instanciaResolver.resolverDeFonte(fonte, ctx).stream()
                .filter(Objects::nonNull)
                .map(EntidadeInstancia::getId)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<UUID> paraListaUuid(Object obj) {
        List<UUID> out = new ArrayList<>();
        if (obj instanceof List<?> list) {
            for (Object item : list) {
                UUID uuid = converterParaUuidSeguro(item);
                if (uuid != null) out.add(uuid);
            }
        } else if (obj != null) {
            UUID uuid = converterParaUuidSeguro(obj);
            if (uuid != null) out.add(uuid);
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