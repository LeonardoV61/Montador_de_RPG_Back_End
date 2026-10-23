package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.application.request.sistema.AtualizarSistemaCommand;

public interface AtualizarSistemaUseCase {
    void executar(AtualizarSistemaCommand command);
}