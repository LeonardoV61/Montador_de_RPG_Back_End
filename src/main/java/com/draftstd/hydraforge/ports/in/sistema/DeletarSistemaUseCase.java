package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.application.request.sistema.DeletarSistemaCommand;

public interface DeletarSistemaUseCase {
    void executar(DeletarSistemaCommand command);
}