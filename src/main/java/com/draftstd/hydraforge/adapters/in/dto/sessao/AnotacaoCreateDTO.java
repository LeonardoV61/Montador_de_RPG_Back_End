package com.draftstd.hydraforge.adapters.in.dto.sessao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AnotacaoCreateDTO(@NotNull(message = "O ID da sessão é obrigatório") UUID sessaoId, @NotBlank(message = "O título é obrigatório") String titulo, @NotBlank(message = "O conteúdo é obrigatório") String conteudo, Boolean privada) {
    public Boolean ePrivado() {
        return privada;
    }
}