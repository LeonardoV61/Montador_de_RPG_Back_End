package com.draftstd.hydraforge.adapters.in.dto.entidade;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

public record EntidadeInstanciaCreateDTO(UUID campanhaId, @NotNull(message = "O ID do sistema é obrigatorio") UUID entidadeSistemaId, @NotBlank(message = "O Tipo é obrigatorio") String tipo, @NotBlank(message = "O nome é obrigatorio") String nome, String descricao, @NotNull(message = "Os atributos são obrigatorios") JsonNode atributosAtuais, JsonNode customizacoes, LocalDateTime criadaEm, String urlImagem) {
    // Método auxiliar mapeado pelo controller
    public JsonNode valoresAtributosJson() {
        return atributosAtuais;
    }
}