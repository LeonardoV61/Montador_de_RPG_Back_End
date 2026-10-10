
package com.draftstd.hydraforge.dto.usuario;

import java.time.LocalDateTime;

public record AmizadeResponseDTO(
        Long remetenteId,
        String remetenteApelido,
        Long destinatarioId,
        String destinatarioApelido,
        String status,
        LocalDateTime criadaEm,
        LocalDateTime aceitoEm
) {}