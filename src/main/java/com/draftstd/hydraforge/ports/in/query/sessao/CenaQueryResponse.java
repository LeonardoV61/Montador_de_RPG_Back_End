package com.draftstd.hydraforge.ports.in.query.sessao;

import java.time.LocalDateTime;
import java.util.UUID;

public record CenaQueryResponse(UUID id, UUID sessaoId, String titulo, String ambiente, String mapaUrl, Boolean ativa, LocalDateTime dataCriacao) {

}