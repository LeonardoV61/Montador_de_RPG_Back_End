package com.draftstd.hydraforge.application.response.sistema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record SistemaQueryResponse(UUID id, UUID usuarioCriadorId, UUID sistemaPaiId, String nome, String descricao, String urlImagem, Map<String, Object> configuracao, Map<String, Object> schemaAtributos, Map<String, Object> schemaEntidades, Map<String, Object> schemaResolucoes, Integer versaoSchemas, boolean eOficial, LocalDateTime criadoEm, List<EntidadeSistemaQueryResponse> entidades) {

}