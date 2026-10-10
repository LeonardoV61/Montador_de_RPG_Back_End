package com.draftstd.hydraforge.ports.in.query.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SessaoQueryResponse(UUID id, UUID campanhaId, String titulo, String descricao, StatusSessao status, LocalDateTime agendadaPara, LocalDateTime dataInicio, LocalDateTime dataFim, List<ParticipanteSessaoResponse> participantes, List<CenaQueryResponse> cenas) {
    public record ParticipanteSessaoResponse(UUID usuarioId, UUID personagemId, Boolean presente) {

    }
}