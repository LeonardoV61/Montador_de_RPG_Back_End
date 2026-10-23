package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.request.sessao.CriarCenaCommand;
import java.util.UUID;

public interface CriarCenaUseCase {
    UUID executar(CriarCenaCommand command);
}