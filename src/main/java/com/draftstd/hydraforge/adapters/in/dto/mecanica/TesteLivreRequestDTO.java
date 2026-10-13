package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public record TesteLivreRequestDTO(@NotNull(message = "O ID da sessão é obrigatório") UUID sessaoId, UUID personagemId, @NotBlank(message = "A expressão dos dados é obrigatória") String expressaoDados, String motivo, Map<String, Object> contextoAdicional) {

}