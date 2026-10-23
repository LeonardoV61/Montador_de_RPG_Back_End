package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record TesteLivreRequestDTO(@NotNull(message = "O ID da sessão é obrigatório") UUID sessaoId, UUID personagemId, @NotBlank(message = "A expressão dos dados é obrigatória") String expressaoDados, List<Integer> dadosSorteados, Integer modificador, String motivo, Map<String, Object> contextoAdicional) {
    public TesteLivreRequestDTO {
        if (dadosSorteados == null) {
            dadosSorteados = List.of();
        }
        if (modificador == null) {
            modificador = 0;
        }
    }
}