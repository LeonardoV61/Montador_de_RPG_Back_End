package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.command.sessao.CriarAnotacaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.AnotacaoQueryResponse;

public interface CriarAnotacaoUseCase {
    AnotacaoQueryResponse executar(CriarAnotacaoCommand command);
}