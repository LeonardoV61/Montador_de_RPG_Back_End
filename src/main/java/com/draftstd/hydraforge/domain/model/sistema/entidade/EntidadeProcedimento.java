package com.draftstd.hydraforge.domain.model.sistema.entidade;

import java.util.Map;
import java.util.UUID;

public class EntidadeProcedimento {

    private UUID id;
    private UUID sistemaId;
    private UUID entidadeSistemaId;
    private UUID entidadeInstanciaId;
    private UUID procedimentoId;
    private String processamento;
    private Map<String, Object> condicao;
    private boolean eReativo;
    private Integer ordem;

    public EntidadeProcedimento() {
        this.eReativo = false;
    }

    public EntidadeProcedimento(UUID id, UUID sistemaId, UUID entidadeSistemaId, UUID entidadeInstanciaId, 
                                UUID procedimentoId, String processamento, Map<String, Object> condicao, 
                                boolean eReativo, Integer ordem) {
        this.id = id != null ? id : UUID.randomUUID();
        this.sistemaId = sistemaId;
        this.entidadeSistemaId = entidadeSistemaId;
        this.entidadeInstanciaId = entidadeInstanciaId;
        this.procedimentoId = procedimentoId;
        this.processamento = processamento;
        this.condicao = condicao != null ? condicao : Map.of();
        this.eReativo = eReativo;
        this.ordem = ordem != null ? ordem : 0;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getSistemaId() { return sistemaId; }
    public void setSistemaId(UUID sistemaId) { this.sistemaId = sistemaId; }

    public UUID getEntidadeSistemaId() { return entidadeSistemaId; }
    public void setEntidadeSistemaId(UUID entidadeSistemaId) { this.entidadeSistemaId = entidadeSistemaId; }

    public UUID getEntidadeInstanciaId() { return entidadeInstanciaId; }
    public void setEntidadeInstanciaId(UUID entidadeInstanciaId) { this.entidadeInstanciaId = entidadeInstanciaId; }

    public UUID getProcedimentoId() { return procedimentoId; }
    public void setProcedimentoId(UUID procedimentoId) { this.procedimentoId = procedimentoId; }

    public String getProcessamento() { return processamento; }
    public void setProcessamento(String processamento) { this.processamento = processamento; }

    public Map<String, Object> getCondicao() { return condicao; }
    public void setCondicao(Map<String, Object> condicao) { this.condicao = condicao; }

    public boolean isEReativo() { return eReativo; }
    public void setEReativo(boolean eReativo) { this.eReativo = eReativo; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }
}