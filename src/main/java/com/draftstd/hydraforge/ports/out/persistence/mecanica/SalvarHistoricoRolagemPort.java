package com.draftstd.hydraforge.ports.out.persistence.mecanica;

import com.draftstd.hydraforge.application.response.mecanica.ResultadoRolagemQueryResponse;

public interface SalvarHistoricoRolagemPort {
    void salvar(ResultadoRolagemQueryResponse resultadoRolagem);
}