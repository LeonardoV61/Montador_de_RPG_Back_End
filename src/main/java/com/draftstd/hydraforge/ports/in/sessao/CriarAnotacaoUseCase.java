package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.request.sessao.CriarAnotacaoCommand;
import java.util.UUID;

public interface CriarAnotacaoUseCase {
    UUID executar(CriarAnotacaoCommand command);
}