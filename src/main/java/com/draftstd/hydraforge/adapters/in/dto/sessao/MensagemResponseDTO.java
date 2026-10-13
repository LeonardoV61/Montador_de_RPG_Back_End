package com.draftstd.hydraforge.adapters.in.dto.sessao;

import java.time.LocalDateTime;
import java.util.UUID;

public record MensagemResponseDTO(UUID id, UUID sessaoId, UUID usuarioId, String conteudo, LocalDateTime momento) {

}