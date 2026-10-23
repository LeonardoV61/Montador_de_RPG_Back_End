package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.request.sessao.EntrarSessaoCommand;

public interface GerenciarParticipantesSessaoUseCase {
    void entrarSessao(EntrarSessaoCommand command);
}