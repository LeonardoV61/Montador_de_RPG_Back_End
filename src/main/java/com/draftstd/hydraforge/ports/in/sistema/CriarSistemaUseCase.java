package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.ports.in.command.sistema.CriarSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;

public interface CriarSistemaUseCase {
    SistemaQueryResponse executar(CriarSistemaCommand command);
}