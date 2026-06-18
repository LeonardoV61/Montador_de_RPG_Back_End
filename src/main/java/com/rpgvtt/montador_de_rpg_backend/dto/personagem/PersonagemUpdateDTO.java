package com.rpgvtt.montador_de_rpg_backend.dto.personagem;

public record PersonagemUpdateDTO(
        String historia,
        String aparencia,
        String urlImagem,
        String notasJogador,
        boolean ativo
) {}