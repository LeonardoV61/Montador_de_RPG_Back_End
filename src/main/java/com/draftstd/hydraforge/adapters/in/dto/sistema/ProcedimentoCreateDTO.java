package com.draftstd.hydraforge.adapters.in.dto.sistema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public record ProcedimentoCreateDTO(@NotNull(message = "O ID do sistema é obrigatório") UUID sistemaId, @NotBlank(message = "O nome é obrigatório") String nome, String descricao, String tipo, Map<String, Object> configsGeral) {

}