package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.command.sessao.CriarCenaCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.CenaQueryResponse;

public interface CriarCenaUseCase {
    CenaQueryResponse executar(CriarCenaCommand command);
}