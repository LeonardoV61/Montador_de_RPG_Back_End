package com.rpgvtt.montador_de_rpg_backend.dto.sistema;

import tools.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventoSistemaCreateDTO(
        @NotNull(message = "O ID do sistema é obrigatório") Long sistemaId,
        @NotBlank(message = "O nome é obrigatório") String nome,
        String descricao,
        @NotNull(message = "O payload schema é obrigatório") JsonNode payloadSchema
) {}