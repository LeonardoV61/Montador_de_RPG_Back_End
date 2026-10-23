package com.draftstd.hydraforge.adapters.in.dto.sessao;

import tools.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CenaCreateDTO(@NotNull(message = "O ID da sessão é obrigatório") UUID sessaoId, @NotBlank(message = "O título é obrigatório") String titulo, String ambiente, String mapaUrl, Integer ordem, String tipo, JsonNode estado) {

}