package com.draftstd.hydraforge.ports.in.mecanica;

import com.draftstd.hydraforge.application.request.mecanica.RolarDadosCommand;
import com.draftstd.hydraforge.application.response.mecanica.ResultadoRolagemQueryResponse;

public interface RolarDadosUseCase {
    ResultadoRolagemQueryResponse executar(RolarDadosCommand command);
}