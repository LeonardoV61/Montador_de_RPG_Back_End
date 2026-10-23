package com.draftstd.hydraforge.adapters.out.persistence.mecanica.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity.PrimitivoJpaEntity;
import com.draftstd.hydraforge.domain.model.mecanica.Primitivo;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class PrimitivoMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PrimitivoJpaEntity toEntity(Primitivo domain) {
        if (domain == null) return null;

        JsonNode jsonNode = null;
        if (domain.getParametroSchemasJson() != null && !domain.getParametroSchemasJson().isBlank()) {
            try {
                jsonNode = objectMapper.readTree(domain.getParametroSchemasJson());
            } catch (Exception e) {
                jsonNode = objectMapper.createObjectNode();
            }
        }

        return new PrimitivoJpaEntity(
                domain.getId(),
                domain.getNome(),
                domain.getDescricao(),
                jsonNode
        );
    }

    public Primitivo toDomain(PrimitivoJpaEntity entity) {
        if (entity == null) return null;

        String schemaJson = entity.getParametroSchemas() != null ? entity.getParametroSchemas().toString() : null;

        return new Primitivo(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                schemaJson
        );
    }
}