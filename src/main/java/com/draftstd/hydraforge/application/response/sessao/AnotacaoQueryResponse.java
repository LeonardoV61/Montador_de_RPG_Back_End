package com.draftstd.hydraforge.application.response.sessao;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnotacaoQueryResponse(UUID id, UUID sessaoId, UUID autorId, String titulo, String conteudo, Boolean privada, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {

}