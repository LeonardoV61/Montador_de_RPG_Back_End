package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.ports.in.command.sistema.ExecutarProcedimentoCommand;

public interface GerenciarProcedimentoUseCase {
    Object executarProcedimento(ExecutarProcedimentoCommand command);
}