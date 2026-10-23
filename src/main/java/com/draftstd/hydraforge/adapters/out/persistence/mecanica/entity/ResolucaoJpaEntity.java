package com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.util.UUID;

@Entity
@Table(name = "resolucoes")
public class ResolucaoJpaEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @NotNull
    @Column(name = "sistema_id", nullable = false)
    private UUID sistemaId;

    @NotNull
    private String nome;

    private String tipo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode parametros;

    public ResolucaoJpaEntity() {
    }

    public ResolucaoJpaEntity(UUID id, UUID sistemaId, String nome, String tipo, JsonNode parametros) {
        this.id = id;
        this.sistemaId = sistemaId;
        this.nome = nome;
        this.tipo = tipo;
        this.parametros = parametros;
    }

    public UUID getId() { return id; }
    public UUID getSistemaId() { return sistemaId; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public JsonNode getParametros() { return parametros; }

    public void setId(UUID id) { this.id = id; }
    public void setSistemaId(UUID sistemaId) { this.sistemaId = sistemaId; }
    public void setNome(String nome) { this.nome = nome; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setParametros(JsonNode parametros) { this.parametros = parametros; }
}