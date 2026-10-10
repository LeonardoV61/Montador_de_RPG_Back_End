package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.command.sessao.AtualizarAnotacaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.AnotacaoQueryResponse;

public interface GerenciarAnotacaoUseCase {
    AnotacaoQueryResponse atualizar(AtualizarAnotacaoCommand command);
}