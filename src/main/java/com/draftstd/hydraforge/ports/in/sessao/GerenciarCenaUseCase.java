package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.command.sessao.AtualizarCenaCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.CenaQueryResponse;

public interface GerenciarCenaUseCase {
    CenaQueryResponse atualizar(AtualizarCenaCommand command);
}