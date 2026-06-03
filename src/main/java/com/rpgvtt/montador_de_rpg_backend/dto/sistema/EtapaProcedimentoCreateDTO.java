package com.rpgvtt.montador_de_rpg_backend.dto.sistema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

public record EtapaProcedimentoCreateDTO(
        @NotNull(message = "A ordem é obrigatória") Integer ordem,
        @NotBlank(message = "O nome é obrigatório") String nome,
        String tipoEtapa,
        @NotNull(message = "Os parâmetros da etapa são obrigatórios") JsonNode parametrosEtapa,
        @NotNull(message = "O campo obrigatório é obrigatório") Boolean obrigatorio
) {}