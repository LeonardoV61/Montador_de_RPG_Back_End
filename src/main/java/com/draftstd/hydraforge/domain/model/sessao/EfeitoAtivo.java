package com.draftstd.hydraforge.domain.model.sessao;

import java.util.Objects;
import java.util.UUID;

public class EfeitoAtivo {

    private final UUID id;
    private final UUID entidadeInstanciaId;
    private final UUID entidadeProcedimentoId;
    private String parametrosJson;
    private String momento;
    private Integer expiraEm;
    private boolean ativo;
    private Integer usosRestantes;

    public EfeitoAtivo(UUID id, UUID entidadeInstanciaId, UUID entidadeProcedimentoId, 
                       String parametrosJson, String momento, Integer expiraEm, 
                       boolean ativo, Integer usosRestantes) {
        this.id = Objects.requireNonNull(id, "O ID do efeito ativo é obrigatório");
        this.entidadeInstanciaId = entidadeInstanciaId;
        this.entidadeProcedimentoId = entidadeProcedimentoId;
        this.parametrosJson = parametrosJson;
        this.momento = momento;
        this.expiraEm = expiraEm;
        this.ativo = ativo;
        this.usosRestantes = usosRestantes;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void consumirUso() {
        if (usosRestantes != null && usosRestantes > 0) {
            usosRestantes--;
            if (usosRestantes == 0) {
                desativar();
            }
        }
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getEntidadeInstanciaId() { return entidadeInstanciaId; }
    public UUID getEntidadeProcedimentoId() { return entidadeProcedimentoId; }
    public String getParametrosJson() { return parametrosJson; }
    public String getMomento() { return momento; }
    public Integer getExpiraEm() { return expiraEm; }
    public boolean isAtivo() { return ativo; }
    public Integer getUsosRestantes() { return usosRestantes; }
}