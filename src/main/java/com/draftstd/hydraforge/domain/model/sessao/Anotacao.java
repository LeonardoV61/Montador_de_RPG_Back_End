package com.draftstd.hydraforge.domain.model.sessao;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Anotacao {

    private final UUID id;
    private final UUID usuarioId;
    private final UUID campanhaId;
    private final UUID sessaoId;
    private String titulo;
    private String conteudo;
    private String categoria;
    private String anotacaoComplexaJson;
    private Boolean ePrivado;
    private final LocalDateTime criadaEm;
    private LocalDateTime atualizadaEm;

    public Anotacao(UUID id, UUID usuarioId, UUID campanhaId, UUID sessaoId, String titulo, String conteudo, String categoria, String anotacaoComplexaJson, Boolean ePrivado) {
        this.id = Objects.requireNonNull(id, "O ID da anotação é obrigatório");
        this.usuarioId = Objects.requireNonNull(usuarioId, "O ID do usuário é obrigatório");
        this.campanhaId = Objects.requireNonNull(campanhaId, "O ID da campanha é obrigatório");
        this.sessaoId = sessaoId;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.categoria = categoria;
        this.anotacaoComplexaJson = anotacaoComplexaJson;
        this.ePrivado = ePrivado != null ? ePrivado : true;
        this.criadaEm = LocalDateTime.now();
        this.atualizadaEm = this.criadaEm;
    }

    public void atualizar(String titulo, String conteudo, String categoria, Boolean ePrivado) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.categoria = categoria;
        if (ePrivado != null) {
            this.ePrivado = ePrivado;
        }
        this.atualizadaEm = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getUsuarioId() { return usuarioId; }
    public UUID getCampanhaId() { return campanhaId; }
    public UUID getSessaoId() { return sessaoId; }
    public String getTitulo() { return titulo; }
    public String getConteudo() { return conteudo; }
    public String getCategoria() { return categoria; }
    public String getAnotacaoComplexaJson() { return anotacaoComplexaJson; }
    public Boolean getEPrivado() { return ePrivado; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public LocalDateTime getAtualizadaEm() { return atualizadaEm; }
}