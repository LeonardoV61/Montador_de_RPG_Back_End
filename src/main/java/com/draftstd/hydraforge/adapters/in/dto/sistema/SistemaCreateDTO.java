package com.draftstd.hydraforge.adapters.in.dto.sistema;

import tools.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SistemaCreateDTO(@NotNull(message = "O ID do criador é obrigatório") UUID criadorId, UUID sistemaPaiId, @NotBlank(message = "O nome é obrigatório") String nome, String descricao, String urlImagem, String versao, JsonNode regrasBaseJson, JsonNode configuracao, JsonNode schemaAtributos, JsonNode schemaEntidades) {

}