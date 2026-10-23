package com.draftstd.hydraforge.adapters.in.dto.sistema.entidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record EntidadeInstanciaCreateDTO(UUID campanhaId, @NotNull(message = "O ID do sistema de entidade é obrigatório") UUID entidadeSistemaId, @NotBlank(message = "O tipo é obrigatório") String tipo, @NotBlank(message = "O nome é obrigatório") String nome, String descricao, String urlImagem, @NotNull(message = "Os atributos são obrigatórios") Map<String, Object> atributosAtuais, Map<String, Object> customizacoes, LocalDateTime criadaEm) {
    public Map<String, Object> valoresAtributos() {
        return atributosAtuais;
    }
}