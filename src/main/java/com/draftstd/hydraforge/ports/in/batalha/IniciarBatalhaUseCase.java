package com.draftstd.hydraforge.ports.in.batalha;

import com.draftstd.hydraforge.ports.in.command.batalha.IniciarBatalhaCommand;
import com.draftstd.hydraforge.ports.in.query.batalha.BatalhaQueryResponse;

public interface IniciarBatalhaUseCase {
    BatalhaQueryResponse executar(IniciarBatalhaCommand command);
}