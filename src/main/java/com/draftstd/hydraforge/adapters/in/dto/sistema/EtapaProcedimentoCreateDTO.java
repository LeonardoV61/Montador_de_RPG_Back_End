package com.draftstd.hydraforge.adapters.in.dto.sistema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record EtapaProcedimentoCreateDTO(@NotNull(message = "A ordem é obrigatória") Integer ordem, @NotBlank(message = "O nome é obrigatório") String nome, String tipoEtapa, @NotNull(message = "Os parâmetros da etapa são obrigatórios") Map<String, Object> parametrosEtapa, @NotNull(message = "O campo obrigatório é obrigatório") Boolean obrigatorio) {

}