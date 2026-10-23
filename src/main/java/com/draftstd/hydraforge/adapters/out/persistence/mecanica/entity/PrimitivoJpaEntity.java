package com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.util.UUID;

@Entity
@Table(name = "primitivos")
public class PrimitivoJpaEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @NotNull
    private String nome;

    private String descricao;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode parametroSchemas;

    public PrimitivoJpaEntity() {
    }

    public PrimitivoJpaEntity(UUID id, String nome, String descricao, JsonNode parametroSchemas) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.parametroSchemas = parametroSchemas;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public JsonNode getParametroSchemas() { return parametroSchemas; }

    public void setId(UUID id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setParametroSchemas(JsonNode parametroSchemas) { this.parametroSchemas = parametroSchemas; }
}