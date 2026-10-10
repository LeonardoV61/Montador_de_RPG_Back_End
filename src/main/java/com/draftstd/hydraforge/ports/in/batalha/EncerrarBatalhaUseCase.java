package com.draftstd.hydraforge.ports.in.batalha;

import com.draftstd.hydraforge.ports.in.command.batalha.EncerrarBatalhaCommand;
import com.draftstd.hydraforge.ports.in.query.batalha.BatalhaQueryResponse;

public interface EncerrarBatalhaUseCase {
    BatalhaQueryResponse executar(EncerrarBatalhaCommand command);
}