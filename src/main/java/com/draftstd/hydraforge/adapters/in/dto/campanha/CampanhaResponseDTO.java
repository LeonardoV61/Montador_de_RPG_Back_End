package com.draftstd.hydraforge.adapters.in.dto.campanha;

import java.time.LocalDateTime;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import java.util.UUID;

public record CampanhaResponseDTO(UUID id, String nome, LocalDateTime criadaEm, UUID sistemaId, String sistemaNome, StatusCampanha Status) {
    
}