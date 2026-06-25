package com.rpgvtt.montador_de_rpg_backend.dto.campanha;

import java.time.LocalDateTime;

import com.rpgvtt.montador_de_rpg_backend.domain.enums.StatusCampanha;

public record CampanhaResponseDTO(Long id,String nome,LocalDateTime criadaEm, Long sistemaId, String sistemaNome, StatusCampanha Status) {
    
}