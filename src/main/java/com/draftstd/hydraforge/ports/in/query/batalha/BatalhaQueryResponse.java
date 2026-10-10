package com.draftstd.hydraforge.ports.in.query.batalha;

import com.draftstd.hydraforge.domain.enums.BatalhaStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BatalhaQueryResponse(UUID id, UUID cenaId, BatalhaStatus status, Integer turnoAtual, UUID participanteTurnoAtualId, List<ParticipanteBatalhaResponse> participantes, LocalDateTime dataCriacao) {
    public record ParticipanteBatalhaResponse(UUID entidadeInstanciaId, String nome, String tipo, Integer iniciativa, Boolean ativo) {

    }
}