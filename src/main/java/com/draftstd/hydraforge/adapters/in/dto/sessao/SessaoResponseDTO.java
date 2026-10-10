package com.draftstd.hydraforge.adapters.in.dto.sessao;

import java.time.LocalDateTime;

/**
 * Record que devolve os dados simplificados da sessão para o frontend.
 */
public record SessaoResponseDTO(Long id, Long campanhaId, LocalDateTime dataInicio, LocalDateTime dataFim) {
    
}