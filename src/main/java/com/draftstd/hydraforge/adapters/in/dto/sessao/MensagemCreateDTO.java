package com.draftstd.hydraforge.adapters.in.dto.sessao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MensagemCreateDTO(@NotNull(message = "O ID da sessão é obrigatório") UUID sessaoId, UUID usuarioId, UUID campanhaId, @NotBlank(message = "O conteúdo da mensagem não pode estar vazio") String conteudo) {

}