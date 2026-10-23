package com.draftstd.hydraforge.adapters.in.dto.campanha;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

public record CampanhaParticipanteResponseDTO(UUID idCampanha, UUID idUsuario, String papel, LocalDateTime entrouEm, String apelido) {
    
}