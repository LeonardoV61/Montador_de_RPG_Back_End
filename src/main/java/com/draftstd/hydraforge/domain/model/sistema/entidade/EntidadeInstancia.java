package com.draftstd.hydraforge.domain.model.sistema.entidade;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class EntidadeInstancia {

    private UUID id;
    private UUID campanhaId;
    private UUID entidadeSistemaId;
    private UUID donoId;
    private String tipo;
    private String nome;
    private String descricao;
    private String urlImagem;
    private Map<String, Object> valoresAtributos;
    private Map<String, Object> customizacoes;
    private LocalDateTime criadaEm;

    public EntidadeInstancia() {}

    public EntidadeInstancia(UUID id, UUID campanhaId, UUID entidadeSistemaId, UUID donoId, String tipo, 
                             String nome, String descricao, String urlImagem, Map<String, Object> valoresAtributos, 
                             Map<String, Object> customizacoes, LocalDateTime criadaEm) {
        this.id = id != null ? id : UUID.randomUUID();
        this.campanhaId = campanhaId;
        this.entidadeSistemaId = entidadeSistemaId;
        this.donoId = donoId;
        this.tipo = tipo;
        this.nome = nome;
        this.descricao = descricao;
        this.urlImagem = urlImagem;
        this.valoresAtributos = valoresAtributos != null ? valoresAtributos : Map.of();
        this.customizacoes = customizacoes != null ? customizacoes : Map.of();
        this.criadaEm = criadaEm != null ? criadaEm : LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCampanhaId() { return campanhaId; }
    public void setCampanhaId(UUID campanhaId) { this.campanhaId = campanhaId; }

    public UUID getEntidadeSistemaId() { return entidadeSistemaId; }
    public void setEntidadeSistemaId(UUID entidadeSistemaId) { this.entidadeSistemaId = entidadeSistemaId; }

    public UUID getDonoId() { return donoId; }
    public void setDonoId(UUID donoId) { this.donoId = donoId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    public Map<String, Object> getValoresAtributos() { return valoresAtributos; }
    public void setValoresAtributos(Map<String, Object> valoresAtributos) { this.valoresAtributos = valoresAtributos; }

    public Map<String, Object> getCustomizacoes() { return customizacoes; }
    public void setCustomizacoes(Map<String, Object> customizacoes) { this.customizacoes = customizacoes; }

    public LocalDateTime getCriadaEm() { return criadaEm; }
    public void setCriadaEm(LocalDateTime criadaEm) { this.criadaEm = criadaEm; }
}