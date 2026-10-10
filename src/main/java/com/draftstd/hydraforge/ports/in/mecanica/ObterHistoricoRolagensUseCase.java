package com.draftstd.hydraforge.ports.in.mecanica;

import com.draftstd.hydraforge.ports.in.query.mecanica.ObterHistoricoRolagensQuery;
import com.draftstd.hydraforge.ports.in.query.mecanica.ResultadoRolagemQueryResponse;
import java.util.List;

public interface ObterHistoricoRolagensUseCase {
    List<ResultadoRolagemQueryResponse> executar(ObterHistoricoRolagensQuery query);
}