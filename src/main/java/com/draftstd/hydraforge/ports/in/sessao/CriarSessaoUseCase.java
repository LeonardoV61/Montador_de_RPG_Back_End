package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.command.sessao.CriarSessaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;

public interface CriarSessaoUseCase {
    SessaoQueryResponse executar(CriarSessaoCommand command);
}