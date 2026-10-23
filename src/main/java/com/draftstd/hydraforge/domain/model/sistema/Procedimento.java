package com.draftstd.hydraforge.domain.model.sistema;

import com.draftstd.hydraforge.domain.enums.StatusProcedimento;
import java.util.Map;
import java.util.UUID;

public class Procedimento {

    private UUID id;
    private UUID sistemaId;
    private String tipo;
    private String nome;
    private String descricao;
    private StatusProcedimento status;
    private Map<String, Object> configsGeral;

    public Procedimento() {}

    public Procedimento(UUID id, UUID sistemaId, String tipo, String nome, String descricao, 
                        StatusProcedimento status, Map<String, Object> configsGeral) {
        this.id = id != null ? id : UUID.randomUUID();
        this.sistemaId = sistemaId;
        this.tipo = tipo;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status != null ? status : StatusProcedimento.ATIVO;
        this.configsGeral = configsGeral != null ? configsGeral : Map.of();
    }

    public UUID getId() { return id; }
    public UUID getSistemaId() { return sistemaId; }
    public String getTipo() { return tipo; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public StatusProcedimento getStatus() { return status; }
    public Map<String, Object> getConfigsGeral() { return configsGeral; }
}