package com.rpgvtt.montador_de_rpg_backend.dto.entidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

public record EntidadeSistemaCreateDTO(
        @NotNull(message = "O ID do sistema é obrigatório") Long sistemaId,
        @NotBlank(message = "O tipo é obrigatório") String tipo,
        @NotBlank(message = "O nome é obrigatório") String nome,
        String descricao,
        String urlImagem,
        @NotNull(message = "Os atributos são obrigatórios") JsonNode atributos,
        @NotNull(message = "As propriedades são obrigatórias") JsonNode propriedades
) {}