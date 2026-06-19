package com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto;

import com.rpgvtt.montador_de_rpg_backend.domain.model.personagem.Personagem;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EscopoInstancias;

import java.util.List;
import java.util.Map;

public record LoadRequest(
        Long idProcedimento,
        Long idSessao,
        EscopoInstancias escopo,
        String               retornoContexto,      // null for root procedures
        Map<String, Object> contextoInicial,      // keys to seed from parent
        List<Personagem> participantesHerdados // null = query DB
) {
    // Compact constructor — normalize nulls
    public LoadRequest {
        contextoInicial = contextoInicial != null ? contextoInicial : Map.of();
    }

    // ── Factory methods for common cases ─────────────────────

    /** Root procedure started by engine — queries participants from DB */
    public static LoadRequest raiz(Long idProcedimento,
                                   Long idSessao,
                                   EscopoInstancias escopo) {
        return new LoadRequest(
                idProcedimento, idSessao, escopo,
                null, null, null
        );
    }

    /** Root with no instance scope (INICIO_COMBATE, ENCERRAR_SESSAO) */
    public static LoadRequest semInstancia(Long idProcedimento,
                                           Long idSessao) {
        return new LoadRequest(
                idProcedimento, idSessao,
                EscopoInstancias.nenhuma(),
                null, null, null
        );
    }

    /** Child procedure called by CHAMAR_PROCEDIMENTO handler */
    public static LoadRequest filho(Long idProcedimento,
                                    ProcedimentoContexto paiCtx,
                                    EscopoInstancias escopo,
                                    String retornoContexto,
                                    Map<String, Object> contextoInicial) {
        return new LoadRequest(
                idProcedimento,
                paiCtx.getIdSessao(),
                escopo,
                retornoContexto,
                contextoInicial,
                paiCtx.getParticipantes() // reuse — no DB call
        );
    }
}
