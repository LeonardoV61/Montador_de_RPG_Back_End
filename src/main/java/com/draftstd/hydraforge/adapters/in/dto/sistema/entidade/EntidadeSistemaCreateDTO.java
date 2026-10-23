package com.draftstd.hydraforge.adapters.in.dto.sistema.entidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public record EntidadeSistemaCreateDTO(@NotNull(message = "O ID do sistema é obrigatório") UUID sistemaId, @NotBlank(message = "O tipo é obrigatório") String tipo, @NotBlank(message = "O nome é obrigatório") String nome, String descricao, String urlImagem, @NotNull(message = "Os atributos são obrigatórios") Map<String, Object> atributos, Map<String, Object> propriedades) {
    public Map<String, Object> schema() {
        return atributos;
    }
}