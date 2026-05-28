package com.rpgvtt.montador_de_rpg_backend.domain.engine.primitivos;

import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.EtapaProcedimento;
import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.Procedimento;
import java.util.*;

public class FrameExecucao {
    private Procedimento procedimento;
    private int indiceEtapaAtual;
    private Map<String, Object> variaveis;

    public FrameExecucao(Procedimento procedimento) {
        this.procedimento = procedimento;
        this.indiceEtapaAtual = 0;
        this.variaveis = new HashMap<>();
        // Ordena as etapas por ordem crescente (campo 'ordem')
        this.procedimento.getEtapas().sort(Comparator.comparingInt(EtapaProcedimento::getOrdem));
    }

    public Procedimento getProcedimento() { return procedimento; }
    public int getIndiceEtapaAtual() { return indiceEtapaAtual; }
    public void setIndiceEtapaAtual(int indice) { this.indiceEtapaAtual = indice; }
    public Map<String, Object> getVariaveis() { return variaveis; }
}