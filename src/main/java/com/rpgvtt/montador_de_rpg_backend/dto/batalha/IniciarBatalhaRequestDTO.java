package com.rpgvtt.montador_de_rpg_backend.dto.batalha;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record IniciarBatalhaRequestDTO (
        @NotNull Long idSessao,
        Long idCena,
        @NotNull List<Long> idsInstanciasJogadores,
        @NotNull List<Long> idsInstanciasInimigos,
        List<List<Long>> lados
        ) {}
