package com.draftstd.hydraforge.adapters.in.dto.batalha;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record IniciarBatalhaRequestDTO (
        @NotNull UUID idSessao,
        UUID idCena,
        @NotNull List<UUID> idsInstanciasJogadores,
        @NotNull List<UUID> idsInstanciasInimigos,
        List<List<UUID>> lados
        ) {}
