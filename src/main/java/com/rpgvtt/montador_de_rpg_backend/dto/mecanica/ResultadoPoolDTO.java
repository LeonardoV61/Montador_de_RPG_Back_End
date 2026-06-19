package com.rpgvtt.montador_de_rpg_backend.dto.mecanica;

import java.util.List;

public record ResultadoPoolDTO(
        int tamanhoPool,
        List<Integer> rolos,
        int sucessos,
        int falhas,
        boolean critico,
        boolean botch,
        Integer dificuldade
) {}
