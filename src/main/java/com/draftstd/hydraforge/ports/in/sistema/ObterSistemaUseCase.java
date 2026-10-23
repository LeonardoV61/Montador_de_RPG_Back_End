package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.application.response.sistema.ListarSistemasQuery;
import com.draftstd.hydraforge.application.response.sistema.ObterSistemaQuery;
import com.draftstd.hydraforge.application.response.sistema.SistemaQueryResponse;
import java.util.List;

public interface ObterSistemaUseCase {
    SistemaQueryResponse obterPorId(ObterSistemaQuery query);
    List<SistemaQueryResponse> listarSistemas(ListarSistemasQuery query);
}