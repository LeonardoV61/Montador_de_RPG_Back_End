package com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces;

import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ContextoAccessor;

import java.util.List;
import java.util.UUID;

public interface ExecucaoContexto {
    ContextoAccessor getContexto();
    Sistema getSistema();
    UUID getIdSistema();
    EscopoInstancias getEscopo();

    void setEscopo(EscopoInstancias escopo);

    boolean semInstancias();
    boolean temInstanciaUnica();
    UUID idInstanciaAtiva();      // Alterado de Long para UUID
    List<UUID> idsInstancias();   // Alterado de List<Long> para List<UUID>
    Procedimento getProcedimento();
}