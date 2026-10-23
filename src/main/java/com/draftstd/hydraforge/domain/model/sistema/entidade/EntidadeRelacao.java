package com.draftstd.hydraforge.domain.model.sistema.entidade;

import java.util.Map;
import java.util.UUID;

public class EntidadeRelacao {

    private UUID id;
    private UUID entidadePaiId;
    private UUID entidadeFilhaId;
    private UUID entidadeSistemaFilhaId;
    private Integer quantidade;
    private Map<String, Object> customizacoes;
    private String origem;

    public EntidadeRelacao() {}

    public EntidadeRelacao(UUID id, UUID entidadePaiId, UUID entidadeFilhaId, UUID entidadeSistemaFilhaId, 
                           Integer quantidade, Map<String, Object> customizacoes, String origem) {
        this.id = id != null ? id : UUID.randomUUID();
        this.entidadePaiId = entidadePaiId;
        this.entidadeFilhaId = entidadeFilhaId;
        this.entidadeSistemaFilhaId = entidadeSistemaFilhaId;
        this.quantidade = quantidade != null ? quantidade : 1;
        this.customizacoes = customizacoes != null ? customizacoes : Map.of();
        this.origem = origem;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getEntidadePaiId() { return entidadePaiId; }
    public void setEntidadePaiId(UUID entidadePaiId) { this.entidadePaiId = entidadePaiId; }

    public UUID getEntidadeFilhaId() { return entidadeFilhaId; }
    public void setEntidadeFilhaId(UUID entidadeFilhaId) { this.entidadeFilhaId = entidadeFilhaId; }

    public UUID getEntidadeSistemaFilhaId() { return entidadeSistemaFilhaId; }
    public void setEntidadeSistemaFilhaId(UUID entidadeSistemaFilhaId) { this.entidadeSistemaFilhaId = entidadeSistemaFilhaId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Map<String, Object> getCustomizacoes() { return customizacoes; }
    public void setCustomizacoes(Map<String, Object> customizacoes) { this.customizacoes = customizacoes; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
}