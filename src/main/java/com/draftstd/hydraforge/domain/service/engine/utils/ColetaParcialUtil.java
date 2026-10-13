package com.draftstd.hydraforge.domain.service.engine.utils;

import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Lógica compartilhada de "coletar respostas até o grupo estar completo",
 * usada por qualquer handler que aguarde múltiplos participantes antes de
 * prosseguir (COLETAR_DECLARACOES_PARALELAS, PROCESSAR_GAMBITS).[cite: 13]
 *
 * Convenção de chaves no contexto:[cite: 13]
 *   "_partial_<salvarEm>" -> Map<UUID, Object> das respostas já recebidas[cite: 13]
 *   "respondente_id"      -> id de quem respondeu nesta chamada (setado por responder())[cite: 13]
 *   "<salvarEm>_entrada"  -> valor enviado pelo respondente nesta chamada[cite: 13]
 */
public final class ColetaParcialUtil {

    private ColetaParcialUtil() {}

    @SuppressWarnings("unchecked")
    public static Map<UUID, Object> carregarParcial(ExecucaoContexto ctx, String salvarEm) {
        return (Map<UUID, Object>) ctx.getContexto()
                .get("_partial_" + salvarEm, Object.class)
                .orElseGet(LinkedHashMap::new);
    }

    public static Map<UUID, Object> registrarResposta(ExecucaoContexto ctx, String salvarEm,
                                                      Map<UUID, Object> parcial,
                                                      List<UUID> idsEsperados) {
        Object rawRespondente = ctx.getContexto().get("respondente_id", Object.class).orElse(null);
        UUID respondente = comoUuid(rawRespondente);
        Object entrada    = ctx.getContexto().get(salvarEm + "_entrada", Object.class).orElse(null);

        if (respondente != null && idsEsperados.contains(respondente) && entrada != null) {
            parcial.put(respondente, entrada);
            ctx.getContexto().put("_partial_" + salvarEm, parcial);
            ctx.getContexto().remove("respondente_id");
            ctx.getContexto().remove(salvarEm + "_entrada");
        }
        return parcial;
    }

    public static List<UUID> pendentes(List<UUID> idsEsperados, Map<UUID, Object> parcial) {
        return idsEsperados.stream().filter(id -> !parcial.containsKey(id)).collect(Collectors.toList());
    }

    public static void finalizarColeta(ExecucaoContexto ctx, String salvarEm, Map<UUID, Object> parcial) {
        ctx.getContexto().put(salvarEm, parcial);
        ctx.getContexto().remove("_partial_" + salvarEm);
    }

    /** Coerção defensiva: chaves/valores vindos do contexto podem ser UUID ou String
     *  dependendo de terem passado por um round-trip de (de)serialização JSON. */
    public static UUID comoUuid(Object v) {
        if (v instanceof UUID u) return u;
        if (v instanceof String s && !s.isBlank()) return UUID.fromString(s);
        return null;
    }
}