package com.draftstd.hydraforge.domain.model.usuario;

import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.events.usuario.*;
import com.draftstd.hydraforge.domain.model.AggregateRoot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Usuario extends AggregateRoot<UUID> {
    private String senha;
    private String email;
    private String apelido;
    private String urlImagem;
    private boolean eAdmin;
    private String provider;
    private String providerId;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Usuario() {}

    // Construtor para registo de novo utilizador
    public Usuario(UUID id, String email, String senha, String apelido, String urlImagem, 
                   boolean eAdmin, String provider, String providerId) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail não pode ser vazio.");
        }

        UUID usuarioId = id != null ? id : UUID.randomUUID();
        this.senha = senha;

        raiseEvent(new UsuarioRegistradoEvent(
            UUID.randomUUID(),
            usuarioId.toString(),
            email.trim().toLowerCase(),
            apelido != null ? apelido.trim() : null,
            provider,
            providerId,
            eAdmin,
            getVersion() + 1,
            Instant.now(),
            null
        ));
    }

    @Override
    protected void apply(DomainEvent event) {
        if (event instanceof UsuarioRegistradoEvent e) {
            setId(UUID.fromString(e.getAggregateId()));
            this.email = e.email();
            this.apelido = e.apelido();
            this.provider = e.provider();
            this.providerId = e.providerId();
            this.eAdmin = e.eAdmin();
            this.criadoEm = LocalDateTime.ofInstant(e.getOccurredOn(), ZoneOffset.UTC);
            this.atualizadoEm = this.criadoEm;
        } else if (event instanceof UsuarioAtualizadoEvent e) {
            if (e.apelido() != null) this.apelido = e.apelido();
            if (e.urlImagem() != null) this.urlImagem = e.urlImagem();
            this.atualizadoEm = LocalDateTime.ofInstant(e.getOccurredOn(), ZoneOffset.UTC);
        } else if (event instanceof SolicitacaoAmizadeEnviadaEvent) {
            // Regras adicionais de estado interno se necessário
        } else if (event instanceof SolicitacaoAmizadeRespondidaEvent) {
            // Regras adicionais de estado interno se necessário
        }
    }

    // Métodos de Comportamento
    public void atualizarPerfil(String novoApelido, String novaUrlImagem) {
        raiseEvent(new UsuarioAtualizadoEvent(
            UUID.randomUUID(),
            getId().toString(),
            novoApelido,
            novaUrlImagem,
            getVersion() + 1,
            Instant.now(),
            null
        ));
    }

    public void enviarSolicitacaoAmizade(UUID destinatarioId) {
        if (destinatarioId == null || destinatarioId.equals(getId())) {
            throw new IllegalArgumentException("Solicitação de amizade inválida.");
        }
        raiseEvent(new SolicitacaoAmizadeEnviadaEvent(
            UUID.randomUUID(),
            getId().toString(),
            destinatarioId,
            getVersion() + 1,
            Instant.now(),
            null
        ));
    }

    public void responderSolicitacaoAmizade(UUID remetenteId, StatusAmizade novoStatus) {
        if (remetenteId == null || novoStatus == null) {
            throw new IllegalArgumentException("Parâmetros de resposta de amizade inválidos.");
        }
        raiseEvent(new SolicitacaoAmizadeRespondidaEvent(
            UUID.randomUUID(),
            getId().toString(),
            remetenteId,
            novoStatus,
            getVersion() + 1,
            Instant.now(),
            null
        ));
    }

    public void alterarSenha(String novaSenha) {
        if (novaSenha == null || novaSenha.isBlank()) {
            throw new IllegalArgumentException("A nova senha não pode ser vazia.");
        }
        this.senha = novaSenha;
        this.atualizadoEm = LocalDateTime.now();
    }

    // Getters Imutáveis
    public String getSenha() { return senha; }
    public String getEmail() { return email; }
    public String getApelido() { return apelido; }
    public String getUrlImagem() { return urlImagem; }
    public boolean isEAdmin() { return eAdmin; }
    public String getProvider() { return provider; }
    public String getProviderId() { return providerId; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    public String getNome() {
        return this.apelido != null ? this.apelido : this.email;
    }
}