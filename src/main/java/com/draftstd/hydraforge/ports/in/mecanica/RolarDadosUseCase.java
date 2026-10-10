package com.draftstd.hydraforge.ports.in.mecanica;

import com.draftstd.hydraforge.ports.in.command.mecanica.RolarDadosCommand;
import com.draftstd.hydraforge.ports.in.query.mecanica.ResultadoRolagemQueryResponse;

public interface RolarDadosUseCase {
    ResultadoRolagemQueryResponse executar(RolarDadosCommand command);
}