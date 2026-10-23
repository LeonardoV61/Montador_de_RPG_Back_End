package com.draftstd.hydraforge.domain.model.mecanica;

import java.util.Objects;
import java.util.UUID;

public class Primitivo {
    private final UUID id;
    private String nome;
    private String descricao;
    private String parametroSchemasJson; // Alterado de JsonNode para String bruta

    public Primitivo(UUID id, String nome, String descricao, String parametroSchemasJson) {
        this.id = Objects.requireNonNull(id, "O ID do primitivo é obrigatório.");
        this.setNome(nome);
        this.descricao = descricao;
        this.parametroSchemasJson = parametroSchemasJson;
    }

    public void atualizar(String nome, String descricao, String parametroSchemasJson) {
        this.setNome(nome);
        this.descricao = descricao;
        this.parametroSchemasJson = parametroSchemasJson;
    }

    private void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do primitivo não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    // Getters
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getParametroSchemasJson() { return parametroSchemasJson; }
}