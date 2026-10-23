package com.draftstd.hydraforge.adapters.in.dto.sistema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public record EventoSistemaCreateDTO(@NotNull(message = "O ID do sistema é obrigatório") UUID sistemaId, @NotBlank(message = "O nome é obrigatório") String nome, String descricao, @NotNull(message = "O payload schema é obrigatório") Map<String, Object> payloadSchema) {

}