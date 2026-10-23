package com.draftstd.hydraforge.application.response.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SessaoQueryResponse(UUID id, UUID campanhaId, String titulo, StatusSessao status, Integer ordem, LocalDateTime dataInicio, LocalDateTime dataFim, UUID procedimentoAtivoId, List<MensagemLogQueryResponse> mensagens) {
}