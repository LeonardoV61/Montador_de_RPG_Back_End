package com.rpgvtt.montador_de_rpg_backend.dto.mecanica;

import jakarta.validation.constraints.NotBlank;

public record TesteLivreRequestDTO(
        @NotBlank String atributo,      // "VIG", "CLA", "SPI" for Mythic Bastionland
        @NotBlank String dado,          // typically "d20"
        Integer  dificuldade,           // nullable — just rolls if absent
        String   vantagem
) { }
