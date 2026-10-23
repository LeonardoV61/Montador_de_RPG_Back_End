package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.application.request.sistema.CriarSistemaCommand;
import java.util.UUID;

public interface CriarSistemaUseCase {
    UUID executar(CriarSistemaCommand command);
}