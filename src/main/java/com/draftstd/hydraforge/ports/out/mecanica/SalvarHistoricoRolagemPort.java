package com.draftstd.hydraforge.ports.out.mecanica;

import com.draftstd.hydraforge.ports.in.query.mecanica.ResultadoRolagemQueryResponse;
import java.util.List;
import java.util.UUID;

public interface SalvarHistoricoRolagemPort {
    void salvar(ResultadoRolagemQueryResponse resultadoRolagem);
    List<ResultadoRolagemQueryResponse> buscarPorSessaoId(UUID sessaoId, Integer limite);
}
