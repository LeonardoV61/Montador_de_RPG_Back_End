package com.draftstd.hydraforge.domain.model.sistema;

import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import java.util.Map;
import java.util.UUID;

public class EtapaProcedimento implements EtapaExecutavel {

    private UUID idEtapa;
    private UUID procedimentoId;
    private Integer ordem;
    private String nome;
    private String tipoEtapa;
    private Map<String, Object> parametrosEtapa;
    private Boolean obrigatorio;

    public EtapaProcedimento() {}

    public EtapaProcedimento(UUID idEtapa, UUID procedimentoId, Integer ordem, String nome, 
                             String tipoEtapa, Map<String, Object> parametrosEtapa, Boolean obrigatorio) {
        this.idEtapa = idEtapa != null ? idEtapa : UUID.randomUUID();
        this.procedimentoId = procedimentoId;
        this.ordem = ordem;
        this.nome = nome;
        this.tipoEtapa = tipoEtapa;
        this.parametrosEtapa = parametrosEtapa != null ? parametrosEtapa : Map.of();
        this.obrigatorio = obrigatorio != null ? obrigatorio : true;
    }

    public UUID getIdEtapa() { return idEtapa; }
    public UUID getProcedimentoId() { return procedimentoId; }
    public Integer getOrdem() { return ordem; }
    public String getNome() { return nome; }
    public String getTipoEtapa() { return tipoEtapa; }
    public Map<String, Object> getParametrosEtapa() { return parametrosEtapa; }
    public Boolean isObrigatorio() { return obrigatorio; }
}