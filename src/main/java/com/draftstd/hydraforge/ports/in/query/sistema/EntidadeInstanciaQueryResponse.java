package com.draftstd.hydraforge.ports.in.query.sistema;

import java.time.LocalDateTime;
import java.util.UUID;

public record EntidadeInstanciaQueryResponse(UUID id, UUID entidadeSistemaId, UUID donoId, String nome, String valoresAtributosJson, LocalDateTime dataCriacao) {

}