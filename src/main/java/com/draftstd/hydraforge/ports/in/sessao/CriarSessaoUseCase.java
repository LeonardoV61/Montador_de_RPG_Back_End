package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.request.sessao.CriarSessaoCommand;
import java.util.UUID;

public interface CriarSessaoUseCase {
    UUID executar(CriarSessaoCommand command);
}