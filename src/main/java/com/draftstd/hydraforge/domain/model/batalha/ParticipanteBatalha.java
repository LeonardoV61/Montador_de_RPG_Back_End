package com.draftstd.hydraforge.domain.model.batalha;

import java.util.UUID;

public class ParticipanteBatalha {

    private UUID id;
    private UUID instanciaId;
    private Integer lado;
    private Integer ordemIniciativa;
    private Integer resultadoIniciativa;
    private boolean ativo;

    public ParticipanteBatalha() {
    }

    public ParticipanteBatalha(UUID id, UUID instanciaId, Integer lado, Integer ordemIniciativa, Integer resultadoIniciativa, boolean ativo) {
        this.id = id;
        this.instanciaId = instanciaId;
        this.lado = lado;
        this.ordemIniciativa = ordemIniciativa;
        this.resultadoIniciativa = resultadoIniciativa;
        this.ativo = ativo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getInstanciaId() {
        return instanciaId;
    }

    public void setInstanciaId(UUID instanciaId) {
        this.instanciaId = instanciaId;
    }

    public Integer getLado() {
        return lado;
    }

    public void setLado(Integer lado) {
        this.lado = lado;
    }

    public Integer getOrdemIniciativa() {
        return ordemIniciativa;
    }

    public void setOrdemIniciativa(Integer ordemIniciativa) {
        this.ordemIniciativa = ordemIniciativa;
    }

    public Integer getResultadoIniciativa() {
        return resultadoIniciativa;
    }

    public void setResultadoIniciativa(Integer resultadoIniciativa) {
        this.resultadoIniciativa = resultadoIniciativa;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}