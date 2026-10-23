package com.draftstd.hydraforge.domain.model.mecanica;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;

import java.util.Objects;
import java.util.UUID;

public class Resolucao {
    private final UUID id;
    private final Sistema sistema;
    private String nome;
    private String tipo;
    private String parametrosJson; // Alterado de JsonNode para String bruta

    public Resolucao(UUID id, Sistema sistema, String nome, String tipo, String parametrosJson) {
        this.id = Objects.requireNonNull(id, "O ID da resolução é obrigatório.");
        this.sistema = Objects.requireNonNull(sistema, "O sistema associado é obrigatório.");
        this.setNome(nome);
        this.tipo = tipo;
        this.parametrosJson = parametrosJson;
    }

    public void atualizarParametros(String novoTipo, String novosParametrosJson) {
        this.tipo = novoTipo;
        this.parametrosJson = novosParametrosJson;
    }

    private void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da resolução não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    // Getters
    public UUID getId() { return id; }
    public Sistema getSistema() { return sistema; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public String getParametrosJson() { return parametrosJson; }
}