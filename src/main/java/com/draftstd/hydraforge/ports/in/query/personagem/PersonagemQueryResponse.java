package com.draftstd.hydraforge.ports.in.query.personagem;

import java.time.LocalDateTime;
import java.util.UUID;

public record PersonagemQueryResponse(UUID id, UUID usuarioId, UUID sistemaId, UUID campanhaId, String nome, String conceito, String dadosJson, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {

}