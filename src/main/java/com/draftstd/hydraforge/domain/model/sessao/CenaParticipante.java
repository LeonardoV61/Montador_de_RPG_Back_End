package com.draftstd.hydraforge.domain.model.sessao;

import java.util.Objects;
import java.util.UUID;

public record CenaParticipante(UUID instanciaId, String posicaoJson, Integer lado) {
    public CenaParticipante {
        Objects.requireNonNull(instanciaId, "O ID da instância do participante é obrigatório");
    }
}