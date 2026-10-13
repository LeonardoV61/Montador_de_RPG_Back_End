package com.draftstd.hydraforge.adapters.in.dto.entidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record EntidadeSistemaCreateDTO(@NotNull(message = "O ID do sistema é obrigatório") UUID sistemaId, @NotBlank(message = "O tipo é obrigatório") String tipo, @NotBlank(message = "O nome é obrigatório") String nome, String descricao, String urlImagem, @NotNull(message = "Os atributos são obrigatórios") JsonNode atributos, @NotNull(message = "As propriedades são obrigatórias") JsonNode propriedades) {
    // Método auxiliar caso o command chame .schemaJson()
    public JsonNode schemaJson() {
        return atributos; // ou combine atributos e propriedades conforme sua regra de negócio
    }
}