package com.draftstd.hydraforge.ports.in.query.sistema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SistemaQueryResponse(UUID id, String nome, String descricao, String versao, UUID autorId, String regrasBaseJson, List<EntidadeSistemaQueryResponse> entidades, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {

}