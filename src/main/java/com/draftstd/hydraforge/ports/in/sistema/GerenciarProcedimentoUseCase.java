package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.application.request.sistema.ExecutarProcedimentoCommand;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;

public interface GerenciarProcedimentoUseCase {
    ResultadoEtapa executarProcedimento(ExecutarProcedimentoCommand command);
}