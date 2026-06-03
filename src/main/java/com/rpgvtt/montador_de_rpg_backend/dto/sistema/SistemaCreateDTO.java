package com.rpgvtt.montador_de_rpg_backend.dto.sistema;

import tools.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SistemaCreateDTO(
        @NotNull(message = "O ID do criador é obrigatório") Long criadorId,
        Long sistemaPaiId,

        @NotBlank(message = "O nome é obrigatório") String nome,
        String descricao,
        String urlImagem,

        @NotNull(message = "A configuração é obrigatória") JsonNode configuracao,
        @NotNull(message = "O schema de atributos é obrigatório") JsonNode schemaAtributos,
        @NotNull(message = "O schema de entidades é obrigatório") JsonNode schemaEntidades
) {}