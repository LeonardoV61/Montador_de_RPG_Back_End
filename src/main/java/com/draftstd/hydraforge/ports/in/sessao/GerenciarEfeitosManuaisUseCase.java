package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.command.sessao.AdicionarEfeitoManualCommand;
import com.draftstd.hydraforge.ports.in.command.sessao.RemoverEfeitoManualCommand;

public interface GerenciarEfeitosManuaisUseCase {
    void adicionarEfeito(AdicionarEfeitoManualCommand command);
    void removerEfeito(RemoverEfeitoManualCommand command);
}