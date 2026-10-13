package com.draftstd.hydraforge.adapters.in.dto.usuario;

import java.util.UUID;
import java.time.LocalDateTime;

public record AmizadeResponseDTO(UUID remetenteId, String remetenteApelido, UUID destinatarioId, String destinatarioApelido, String status, LocalDateTime criadaEm, LocalDateTime aceitoEm) {

}