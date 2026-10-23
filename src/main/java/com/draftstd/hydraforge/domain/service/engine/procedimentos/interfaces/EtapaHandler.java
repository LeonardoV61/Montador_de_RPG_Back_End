package com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces;

import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;

public interface EtapaHandler {

    String tipoEtapa();

    ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx);

}