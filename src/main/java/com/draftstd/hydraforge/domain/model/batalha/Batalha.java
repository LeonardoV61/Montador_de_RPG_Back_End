package com.draftstd.hydraforge.domain.model.batalha;

import com.draftstd.hydraforge.domain.enums.BatalhaStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Batalha {

    private UUID id;
    private UUID sessaoId;
    private UUID campanhaId;
    private int rodadaAtual;
    private int turnoAtual;
    private BatalhaStatus status;
    private List<ParticipanteBatalha> participantes = new ArrayList<>();
    private LocalDateTime inicioEm;
    private LocalDateTime fimEm;

    public Batalha() {
        this.status = BatalhaStatus.INICIANDO;
        this.rodadaAtual = 1;
        this.turnoAtual = 0;
    }

    public Batalha(UUID id, UUID sessaoId, UUID campanhaId, int rodadaAtual, int turnoAtual, BatalhaStatus status, List<ParticipanteBatalha> participantes, LocalDateTime inicioEm, LocalDateTime fimEm) {
        this.id = id;
        this.sessaoId = sessaoId;
        this.campanhaId = campanhaId;
        this.rodadaAtual = rodadaAtual;
        this.turnoAtual = turnoAtual;
        this.status = status;
        this.participantes = participantes != null ? participantes : new ArrayList<>();
        this.inicioEm = inicioEm;
        this.fimEm = fimEm;
    }

    // Regras de Negócio do Domínio

    public void iniciarBatalha() {
        this.status = BatalhaStatus.EM_ANDAMENTO;
        this.inicioEm = LocalDateTime.now();
        this.rodadaAtual = 1;
        this.turnoAtual = 0;
        ordenarIniciativa();
    }

    public void proximoTurno() {
        if (participantes.isEmpty()) return;

        this.turnoAtual++;
        if (this.turnoAtual >= participantes.size()) {
            this.turnoAtual = 0;
            this.rodadaAtual++;
        }
    }

    public void finalizarBatalha() {
        this.status = BatalhaStatus.CONCLUIDA;
        this.fimEm = LocalDateTime.now();
    }

    public void ordenarIniciativa() {
        this.participantes.sort((p1, p2) -> Integer.compare(p2.getOrdemIniciativa(), p1.getOrdemIniciativa()));
        for (int i = 0; i < participantes.size(); i++) {
            participantes.get(i).setOrdemIniciativa(i + 1);
        }
    }

    // Getters e Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSessaoId() {
        return sessaoId;
    }

    public void setSessaoId(UUID sessaoId) {
        this.sessaoId = sessaoId;
    }

    public UUID getCampanhaId() {
        return campanhaId;
    }

    public void setCampanhaId(UUID campanhaId) {
        this.campanhaId = campanhaId;
    }

    public int getRodadaAtual() {
        return rodadaAtual;
    }

    public void setRodadaAtual(int rodadaAtual) {
        this.rodadaAtual = rodadaAtual;
    }

    public int getTurnoAtual() {
        return turnoAtual;
    }

    public void setTurnoAtual(int turnoAtual) {
        this.turnoAtual = turnoAtual;
    }

    public BatalhaStatus getStatus() {
        return status;
    }

    public void setStatus(BatalhaStatus status) {
        this.status = status;
    }

    public List<ParticipanteBatalha> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<ParticipanteBatalha> participantes) {
        this.participantes = participantes;
    }

    public LocalDateTime getInicioEm() {
        return inicioEm;
    }

    public void setInicioEm(LocalDateTime inicioEm) {
        this.inicioEm = inicioEm;
    }

    public LocalDateTime getFimEm() {
        return fimEm;
    }

    public void setFimEm(LocalDateTime fimEm) {
        this.fimEm = fimEm;
    }
}