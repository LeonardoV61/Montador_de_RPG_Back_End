package com.draftstd.hydraforge.adapters.out.persistence.mecanica.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity.ResolucaoJpaEntity;
import com.draftstd.hydraforge.domain.model.mecanica.Resolucao;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResolucaoMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResolucaoJpaEntity toEntity(Resolucao domain) {
        if (domain == null) return null;

        UUID sistemaId = domain.getSistema() != null ? domain.getSistema().getId() : null;

        JsonNode parametrosNode = null;
        if (domain.getParametrosJson() != null && !domain.getParametrosJson().isBlank()) {
            try {
                parametrosNode = objectMapper.readTree(domain.getParametrosJson());
            } catch (Exception e) {
                parametrosNode = objectMapper.createObjectNode();
            }
        }

        return new ResolucaoJpaEntity(
                domain.getId(),
                sistemaId,
                domain.getNome(),
                domain.getTipo(),
                parametrosNode
        );
    }

    public Resolucao toDomain(ResolucaoJpaEntity entity) {
        if (entity == null) return null;

        Sistema sistema = entity.getSistemaId() != null ? Sistema.reconstituirChave(entity.getSistemaId()) : null;

        String parametrosJson = entity.getParametros() != null ? entity.getParametros().toString() : null;

        return new Resolucao(
                entity.getId(),
                sistema,
                entity.getNome(),
                entity.getTipo(),
                parametrosJson
        );
    }
}