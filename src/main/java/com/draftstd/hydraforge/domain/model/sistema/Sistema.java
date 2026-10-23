package com.draftstd.hydraforge.domain.model.sistema;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.events.sistema.SistemaAtualizadoEvent;
import com.draftstd.hydraforge.domain.events.sistema.SistemaCriadoEvent;
import com.draftstd.hydraforge.domain.model.AggregateRoot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

public class Sistema extends AggregateRoot<UUID> {

    private UUID usuarioCriadorId;
    private UUID sistemaPaiId;
    private String nome;
    private String descricao;
    private String urlImagem;
    private Map<String, Object> configuracao;
    private Map<String, Object> schemaAtributos;
    private Map<String, Object> schemaEntidades;
    private Map<String, Object> schemaResolucoes;
    private Integer versaoSchemas;
    private boolean eOficial;
    private LocalDateTime criadoEm;

    public Sistema() {
        this.versaoSchemas = 1;
        this.eOficial = false;
    }

    /**
     * Método utilitário de reconstituição de referência para mapeamentos da infraestrutura.
     */
    public static Sistema reconstituirChave(UUID id) {
        if (id == null) return null;
        Sistema sistema = new Sistema();
        sistema.setId(id);
        return sistema;
    }

    // Construtor com comportamento do Agregado
    public Sistema(UUID id, UUID usuarioCriadorId, UUID sistemaPaiId, String nome, 
                   String descricao, String urlImagem, Map<String, Object> configuracao, 
                   Map<String, Object> schemaAtributos, Map<String, Object> schemaEntidades, 
                   Map<String, Object> schemaResolucoes, boolean eOficial) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do sistema é obrigatório.");
        }

        UUID sistemaId = id != null ? id : UUID.randomUUID();

        raiseEvent(new SistemaCriadoEvent(
            UUID.randomUUID(),
            sistemaId.toString(),
            usuarioCriadorId,
            sistemaPaiId,
            nome.trim(),
            descricao,
            urlImagem,
            configuracao != null ? configuracao : Map.of(),
            schemaAtributos != null ? schemaAtributos : Map.of(),
            schemaEntidades != null ? schemaEntidades : Map.of(),
            schemaResolucoes != null ? schemaResolucoes : Map.of(),
            eOficial,
            getVersion() + 1,
            Instant.now(),
            null
        ));
    }

    @Override
    protected void apply(DomainEvent event) {
        if (event instanceof SistemaCriadoEvent e) {
            setId(UUID.fromString(e.getAggregateId()));
            this.usuarioCriadorId = e.usuarioCriadorId();
            this.sistemaPaiId = e.sistemaPaiId();
            this.nome = e.nome();
            this.descricao = e.descricao();
            this.urlImagem = e.urlImagem();
            this.configuracao = e.configuracao();
            this.schemaAtributos = e.schemaAtributos();
            this.schemaEntidades = e.schemaEntidades();
            this.schemaResolucoes = e.schemaResolucoes();
            this.eOficial = e.eOficial();
            this.versaoSchemas = 1;
            this.criadoEm = LocalDateTime.ofInstant(e.getOccurredOn(), ZoneOffset.UTC);
        } else if (event instanceof SistemaAtualizadoEvent e) {
            this.nome = e.nome();
            this.descricao = e.descricao();
            this.urlImagem = e.urlImagem();
            this.configuracao = e.configuracao();
            this.schemaAtributos = e.schemaAtributos();
            this.schemaEntidades = e.schemaEntidades();
            this.schemaResolucoes = e.schemaResolucoes();
            this.versaoSchemas = e.novaVersaoSchemas();
        }
    }

    public void atualizar(String novoNome, String novaDescricao, String novaUrlImagem, 
                          Map<String, Object> novaConfig, Map<String, Object> novosAtributos, 
                          Map<String, Object> novasEntidades, Map<String, Object> novasResolucoes) {
        if (novoNome == null || novoNome.isBlank()) {
            throw new IllegalArgumentException("O nome do sistema é obrigatório.");
        }

        raiseEvent(new SistemaAtualizadoEvent(
            UUID.randomUUID(),
            getId().toString(),
            novoNome.trim(),
            novaDescricao,
            novaUrlImagem,
            novaConfig != null ? novaConfig : this.configuracao,
            novosAtributos != null ? novosAtributos : this.schemaAtributos,
            novasEntidades != null ? novasEntidades : this.schemaEntidades,
            novasResolucoes != null ? novasResolucoes : this.schemaResolucoes,
            this.versaoSchemas + 1,
            getVersion() + 1,
            Instant.now(),
            null
        ));
    }

    // Getters Imutáveis
    public UUID getUsuarioCriadorId() { return usuarioCriadorId; }
    public UUID getSistemaPaiId() { return sistemaPaiId; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getUrlImagem() { return urlImagem; }
    public Map<String, Object> getConfiguracao() { return configuracao; }
    public Map<String, Object> getSchemaAtributos() { return schemaAtributos; }
    public Map<String, Object> getSchemaEntidades() { return schemaEntidades; }
    public Map<String, Object> getSchemaResolucoes() { return schemaResolucoes; }
    public Integer getVersaoSchemas() { return versaoSchemas; }
    public boolean isEOficial() { return eOficial; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}