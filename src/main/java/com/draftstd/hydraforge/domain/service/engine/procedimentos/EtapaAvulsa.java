package com.draftstd.hydraforge.domain.service.engine.procedimentos;

import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;

import java.util.Map;

public record EtapaAvulsa(String nome, String tipoEtapa, Map<String, Object> parametrosEtapa, boolean obrigatorio) implements EtapaExecutavel {

    public EtapaAvulsa(String nome, String tipoEtapa, Map<String, Object> parametrosEtapa) {
        this(nome, tipoEtapa, parametrosEtapa, true);
    }

    @Override 
    public String getNome() { return nome; }
    
    @Override 
    public String getTipoEtapa() { return tipoEtapa; }
    
    @Override 
    public Map<String, Object> getParametrosEtapa() { return parametrosEtapa; }

    @Override
    public Boolean isObrigatorio() { return obrigatorio; }

    public static EtapaAvulsa de(String tipoEtapa, Map<String, Object> parametros) {
        return new EtapaAvulsa("Execução avulsa: " + tipoEtapa, tipoEtapa, parametros != null ? parametros : Map.of(), true);
    }

    public static EtapaAvulsa de(String nome, String tipoEtapa, Map<String, Object> parametros) {
        return new EtapaAvulsa(nome, tipoEtapa, parametros != null ? parametros : Map.of(), true);
    }

    public static EtapaAvulsa de(String nome, String tipoEtapa, Map<String, Object> parametros, boolean obrigatorio) {
        return new EtapaAvulsa(nome, tipoEtapa, parametros != null ? parametros : Map.of(), obrigatorio);
    }
}