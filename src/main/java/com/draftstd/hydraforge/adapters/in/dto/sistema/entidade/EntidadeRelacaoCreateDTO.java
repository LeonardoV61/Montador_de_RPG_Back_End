package com.draftstd.hydraforge.adapters.in.dto.sistema.entidade;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Map;
import java.util.UUID;

public record EntidadeRelacaoCreateDTO(@NotNull(message = "O ID da entidade pai é obrigatório") UUID idEntidadePai, @NotNull(message = "O ID da entidade filha é obrigatório") UUID idEntidadeFilha, @NotNull(message = "A quantidade é obrigatória") @Positive(message = "A quantidade deve ser maior que zero") Integer quantidade, Map<String, Object> customizacoes, String origem) {

}