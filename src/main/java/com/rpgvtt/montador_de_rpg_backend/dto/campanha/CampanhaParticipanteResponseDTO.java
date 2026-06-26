package com.rpgvtt.montador_de_rpg_backend.dto.campanha;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampanhaParticipanteResponseDTO {
    private Long campanhaId;
    private Long usuarioId;
    private String papel;
    private LocalDateTime entrouEm;
}