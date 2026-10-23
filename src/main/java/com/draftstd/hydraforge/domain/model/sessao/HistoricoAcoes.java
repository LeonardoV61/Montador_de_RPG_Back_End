package com.draftstd.hydraforge.domain.model.sessao;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record HistoricoAcoes(UUID id, UUID entidadeInstanciaId, LocalDateTime momento, String resultadoJson) {
    public HistoricoAcoes {
        Objects.requireNonNull(id, "O ID do histórico é obrigatório");
        if (momento == null) {
            momento = LocalDateTime.now();
        }
    }

    public UUID getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }
}