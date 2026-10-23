package com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto;

import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EscopoInstancias;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record LoadRequest(
        UUID idProcedimento,
        UUID idSessao,
        EscopoInstancias escopo,
        String               retornoContexto,      // null for root procedures
        Map<String, Object> contextoInicial,      // keys to seed from parent
        List<Personagem> participantesHerdados // null = query DB
) {
    public LoadRequest {
        contextoInicial = contextoInicial != null ? contextoInicial : Map.of();
    }


    public static LoadRequest raiz(UUID idProcedimento,
                                   UUID idSessao,
                                   EscopoInstancias escopo) {
        return new LoadRequest(
                idProcedimento, idSessao, escopo,
                null, null, null
        );
    }

    public static LoadRequest raiz(UUID idProcedimento,
                                   UUID idSessao,
                                   EscopoInstancias escopo,
                                   Map<String, Object> contextoInicial) {
        return new LoadRequest(
                idProcedimento, idSessao, escopo,
                null, contextoInicial, null
        );
    }

    public static LoadRequest semInstancia(UUID idProcedimento,
                                           UUID idSessao) {
        return new LoadRequest(
                idProcedimento, idSessao,
                EscopoInstancias.nenhuma(),
                null, null, null
        );
    }

    public static LoadRequest filho(UUID idProcedimento,
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
