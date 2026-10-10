package com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces;

import tools.jackson.databind.JsonNode;

public interface EtapaExecutavel {
    String getNome();
    String getTipoEtapa();
    JsonNode getParametrosEtapa();
    
}
