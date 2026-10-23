package com.draftstd.hydraforge.application.response.sessao;

import java.time.LocalDateTime;
import java.util.UUID;

public record MensagemLogQueryResponse(UUID id, UUID sessaoId, UUID emissorId,  String conteudo, LocalDateTime enviadaEm) {

}