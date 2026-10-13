package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.command.sessao.EntrarSessaoCommand;

public interface GerenciarParticipantesSessaoUseCase {
    void entrarSessao(EntrarSessaoCommand command);
}