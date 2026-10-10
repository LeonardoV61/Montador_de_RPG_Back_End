package com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces;

import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ContextoAccessor;

import java.util.List;

public interface ExecucaoContexto {
    ContextoAccessor getContexto();
    Sistema getSistema();
    Long getIdSistema();
    EscopoInstancias getEscopo();

    void setEscopo(EscopoInstancias escopo);

    boolean semInstancias();
    boolean temInstanciaUnica();
    Long idInstanciaAtiva();
    List<Long> idsInstancias();
    Procedimento getProcedimento();
}
