package com.draftstd.hydraforge.ports.in.query.batalha;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BatalhaQueryResponse(UUID id, UUID sessaoId, UUID cenaId, String status, int rodadaAtual, LocalDateTime iniciadaEm, List<ParticipanteBatalhaResponse> participantes) {
    public record ParticipanteBatalhaResponse(UUID entidadeInstanciaId, String nome, String tipo, Integer iniciativa, Boolean ativo) {

    }
}
