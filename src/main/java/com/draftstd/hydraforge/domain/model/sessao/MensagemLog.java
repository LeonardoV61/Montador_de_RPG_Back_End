package com.draftstd.hydraforge.domain.model.sessao;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record MensagemLog(UUID id, UUID usuarioId, String conteudo, LocalDateTime momento) {
    public MensagemLog {
        Objects.requireNonNull(id, "O ID da mensagem é obrigatório");
        Objects.requireNonNull(usuarioId, "O ID do usuário é obrigatório");
        Objects.requireNonNull(conteudo, "O conteúdo da mensagem é obrigatório");
        if (momento == null) {
            momento = LocalDateTime.now();
        }
    }
}