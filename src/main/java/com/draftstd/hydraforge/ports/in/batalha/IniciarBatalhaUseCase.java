package com.draftstd.hydraforge.ports.in.batalha;

import com.draftstd.hydraforge.application.request.batalha.IniciarBatalhaCommand;
import com.draftstd.hydraforge.application.response.batalha.BatalhaQueryResponse;

public interface IniciarBatalhaUseCase {
    BatalhaQueryResponse executar(IniciarBatalhaCommand command);
}