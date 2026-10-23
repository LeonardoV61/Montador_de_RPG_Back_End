package com.draftstd.hydraforge.domain.model.personagem;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.events.personagem.*;
import com.draftstd.hydraforge.domain.model.AggregateRoot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Personagem extends AggregateRoot<UUID> {
    private String nome;
    private UUID usuarioId;
    private UUID campanhaId;
    private UUID instanciaId;
    private String historia;
    private boolean ativo;
    private String aparencia;
    private String urlImagem;
    private String notasJogador;
    private LocalDateTime criadoEm;
    private Map<String, Object> atributos = new HashMap<>();

    public Personagem() {}

    // Construtor para criação de novo personagem
    public Personagem(UUID id, String nome, UUID usuarioId, UUID campanhaId, UUID instanciaId, String historia, String aparencia, String urlImagem, String notasJogador, Map<String, Object> atributos) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do personagem não pode ser vazio.");
        }

        UUID personagemId = id != null ? id : UUID.randomUUID();
        Map<String, Object> atributosIniciais = atributos != null ? new HashMap<>(atributos) : new HashMap<>();

        raiseEvent(new PersonagemCriadoEvent(
            UUID.randomUUID(),
            personagemId.toString(),
            usuarioId,
            campanhaId,
            instanciaId,
            nome.trim(),
            historia,
            aparencia,
            urlImagem,
            notasJogador,
            atributosIniciais,
            getVersion() + 1,
            Instant.now(),
            null // sessaoId opcional
        ));
    }

    @Override
    protected void apply(DomainEvent event) {
        if (event instanceof PersonagemCriadoEvent e) {
            setId(UUID.fromString(e.getAggregateId()));
            this.usuarioId = e.usuarioId();
            this.campanhaId = e.campanhaId();
            this.instanciaId = e.instanciaId();
            this.nome = e.nome();
            this.historia = e.historia();
            this.aparencia = e.aparencia();
            this.urlImagem = e.urlImagem();
            this.notasJogador = e.notasJogador();
            this.atributos = e.atributos() != null ? new HashMap<>(e.atributos()) : new HashMap<>();
            this.ativo = true;
            this.criadoEm = LocalDateTime.ofInstant(e.getOccurredOn(), ZoneOffset.UTC);
        } else if (event instanceof PersonagemAtualizadoEvent e) {
            if (e.nome() != null) this.nome = e.nome();
            if (e.historia() != null) this.historia = e.historia();
            if (e.aparencia() != null) this.aparencia = e.aparencia();
            if (e.urlImagem() != null) this.urlImagem = e.urlImagem();
            if (e.notasJogador() != null) this.notasJogador = e.notasJogador();
            if (e.atributos() != null) this.atributos.putAll(e.atributos());
        } else if (event instanceof PersonagemDeletadoEvent) {
            this.ativo = false;
        }
    }

    public void atualizar(String novoNome, String historia, String aparencia, 
                          String urlImagem, String notasJogador, Map<String, Object> novosAtributos, UUID sessaoId) {
        raiseEvent(new PersonagemAtualizadoEvent(
            UUID.randomUUID(),
            getId().toString(),
            novoNome,
            historia,
            aparencia,
            urlImagem,
            notasJogador,
            novosAtributos,
            getVersion() + 1,
            Instant.now(),
            sessaoId
        ));
    }

    public void realizarDescanso(String tipoDescanso, UUID sessaoId) {
        raiseEvent(new DescansoRealizadoEvent(
            UUID.randomUUID(),
            getId().toString(),
            tipoDescanso,
            getVersion() + 1,
            Instant.now(),
            sessaoId
        ));
    }

    public void consumirItem(UUID itemId, UUID sessaoId) {
        raiseEvent(new ItemConsumidoEvent(
            UUID.randomUUID(),
            getId().toString(),
            itemId,
            getVersion() + 1,
            Instant.now(),
            sessaoId
        ));
    }

    public void desativar(UUID sessaoId) {
        if (!this.ativo) return;
        raiseEvent(new PersonagemDeletadoEvent(
            UUID.randomUUID(),
            getId().toString(),
            getVersion() + 1,
            Instant.now(),
            sessaoId
        ));
    }

    // Getters
    public String getNome() { return nome; }
    public UUID getUsuarioId() { return usuarioId; }
    public UUID getCampanhaId() { return campanhaId; }
    public UUID getInstanciaId() { return instanciaId; }
    public String getHistoria() { return historia; }
    public boolean isAtivo() { return ativo; }
    public String getAparencia() { return aparencia; }
    public String getUrlImagem() { return urlImagem; }
    public String getNotasJogador() { return notasJogador; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public Map<String, Object> getAtributos() { return Map.copyOf(atributos); }
}