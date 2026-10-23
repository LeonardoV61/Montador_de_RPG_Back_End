package com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces;

import java.util.Map;

public interface EtapaExecutavel {
    String getNome();
    String getTipoEtapa();
    Map<String, Object> getParametrosEtapa();
    Boolean isObrigatorio();
}