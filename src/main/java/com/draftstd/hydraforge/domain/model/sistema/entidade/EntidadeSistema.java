package com.draftstd.hydraforge.domain.model.sistema.entidade;

import java.util.Map;
import java.util.UUID;

public class EntidadeSistema {

    private UUID id;
    private UUID sistemaId; 
    private String tipo;
    private String nome;
    private String descricao;
    private String urlImagem;
    private Map<String, Object> atributos; 
    private Map<String, Object> propriedades;

    public EntidadeSistema() {}

    public EntidadeSistema(UUID id, UUID sistemaId, String tipo, String nome, String descricao, 
                           String urlImagem, Map<String, Object> atributos, Map<String, Object> propriedades) {
        this.id = id != null ? id : UUID.randomUUID();
        this.sistemaId = sistemaId;
        this.tipo = tipo;
        this.nome = nome;
        this.descricao = descricao;
        this.urlImagem = urlImagem;
        this.atributos = atributos != null ? atributos : Map.of();
        this.propriedades = propriedades != null ? propriedades : Map.of();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getSistemaId() { return sistemaId; }
    public void setSistemaId(UUID sistemaId) { this.sistemaId = sistemaId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    public Map<String, Object> getAtributos() { return atributos; }
    public void setAtributos(Map<String, Object> atributos) { this.atributos = atributos; }

    public Map<String, Object> getPropriedades() { return propriedades; }
    public void setPropriedades(Map<String, Object> propriedades) { this.propriedades = propriedades; }
}