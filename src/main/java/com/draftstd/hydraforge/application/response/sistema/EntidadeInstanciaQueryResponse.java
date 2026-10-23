package com.draftstd.hydraforge.application.response.sistema;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record EntidadeInstanciaQueryResponse(UUID id, UUID campanhaId, UUID entidadeSistemaId, UUID donoId, String tipo, String nome, String descricao, String urlImagem, Map<String, Object> valoresAtributos, Map<String, Object> customizacoes, LocalDateTime criadaEm) {

}