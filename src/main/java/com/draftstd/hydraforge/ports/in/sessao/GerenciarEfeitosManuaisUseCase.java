package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.request.sessao.AdicionarEfeitoManualCommand;
import com.draftstd.hydraforge.application.request.sessao.RemoverEfeitoManualCommand;

public interface GerenciarEfeitosManuaisUseCase {
    void adicionarEfeito(AdicionarEfeitoManualCommand command);
    void removerEfeito(RemoverEfeitoManualCommand command);
}