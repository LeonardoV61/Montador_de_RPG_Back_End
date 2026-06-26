package com.rpgvtt.montador_de_rpg_backend.dto.campanha;

public record PersonagemCampanhaDTO(
        Long personagemId,
        Long instanciaId,
        String nome,
        String tipo
) {}
