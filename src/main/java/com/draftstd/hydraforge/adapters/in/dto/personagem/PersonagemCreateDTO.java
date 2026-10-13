package com.draftstd.hydraforge.adapters.in.dto.personagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record PersonagemCreateDTO(@NotNull(message = "O ID do sistema é obrigatório") UUID sistemaId, UUID campanhaId, @NotBlank(message = "O nome é obrigatório") String nome, String conceito, JsonNode dadosJson, String historia, String aparencia, String urlImagem, String notasJogador) {

}