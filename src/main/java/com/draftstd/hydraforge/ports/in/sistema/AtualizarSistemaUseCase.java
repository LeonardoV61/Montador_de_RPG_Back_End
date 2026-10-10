package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;

public interface AtualizarSistemaUseCase {
    SistemaQueryResponse executar(AtualizarSistemaCommand command);
}