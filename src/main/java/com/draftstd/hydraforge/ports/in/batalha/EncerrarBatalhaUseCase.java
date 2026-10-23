package com.draftstd.hydraforge.ports.in.batalha;

import com.draftstd.hydraforge.application.request.batalha.EncerrarBatalhaCommand;
import com.draftstd.hydraforge.application.response.batalha.BatalhaQueryResponse;

public interface EncerrarBatalhaUseCase {
    BatalhaQueryResponse executar(EncerrarBatalhaCommand command);
}