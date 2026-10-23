package com.draftstd.hydraforge.adapters.in.dto.sessao;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessaoResponseDTO(UUID id, UUID campanhaId, LocalDateTime dataInicio, LocalDateTime dataFim) {
    
}