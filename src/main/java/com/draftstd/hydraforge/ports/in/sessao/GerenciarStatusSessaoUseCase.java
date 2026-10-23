package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.request.sessao.AlterarStatusSessaoCommand;

public interface GerenciarStatusSessaoUseCase {
    void alterarStatus(AlterarStatusSessaoCommand command);
}