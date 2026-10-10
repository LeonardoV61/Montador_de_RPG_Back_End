package com.draftstd.hydraforge.adapters.in.dto.campanha;

import java.time.LocalDateTime;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;

public record CampanhaResponseDTO(Long id,String nome,LocalDateTime criadaEm, Long sistemaId, String sistemaNome, StatusCampanha Status) {
    
}