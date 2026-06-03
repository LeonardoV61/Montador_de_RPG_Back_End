package com.rpgvtt.montador_de_rpg_backend.dto.mecanica;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

public record ResolucaoCreateDTO(
        @NotNull(message = "O ID do sistema é obrigatório") Long sistemaId,
        @NotBlank(message = "O nome é obrigatório") String nome,
        @NotBlank(message = "O tipo é obrigatório") String tipo,
        @NotNull(message = "Os parâmetros são obrigatórios") JsonNode parametros
) {}