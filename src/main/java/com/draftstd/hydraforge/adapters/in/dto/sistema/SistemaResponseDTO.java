package com.draftstd.hydraforge.adapters.in.dto.sistema;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record SistemaResponseDTO(UUID id, UUID criadorId, UUID sistemaPaiId, String nome, String descricao, String urlImagem, Map<String, Object> configuracao, Map<String, Object> schemaAtributos, Map<String, Object> schemaEntidades, Map<String, Object> schemaResolucoes, Integer versaoSchemas, boolean eOficial, LocalDateTime criadoEm) {

}