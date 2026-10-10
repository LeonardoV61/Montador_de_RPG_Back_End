package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.command.sessao.AlterarStatusSessaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;

public interface GerenciarStatusSessaoUseCase {
    SessaoQueryResponse alterarStatus(AlterarStatusSessaoCommand command);
}