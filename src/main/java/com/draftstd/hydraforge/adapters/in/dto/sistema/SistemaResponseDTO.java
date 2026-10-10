// SistemaResponseDTO.java
package com.draftstd.hydraforge.dto.sistema;

import tools.jackson.databind.JsonNode;
import java.time.LocalDateTime;

public record SistemaResponseDTO(
        Long id,
        Long criadorId,
        String criadorApelido,
        Long sistemaPaiId,   // null se for raiz
        String nome,
        String descricao,
        String urlImagem,
        JsonNode configuracao,
        JsonNode schemaAtributos,
        JsonNode schemaEntidades,
        Integer versaoSchemas,
        boolean eOficial,
        LocalDateTime criadoEm
) {}