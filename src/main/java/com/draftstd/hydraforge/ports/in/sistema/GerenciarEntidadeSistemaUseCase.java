package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.application.request.sistema.AtualizarEntidadeSistemaCommand;
import com.draftstd.hydraforge.application.request.sistema.CriarEntidadeSistemaCommand;
import com.draftstd.hydraforge.application.response.sistema.EntidadeSistemaQueryResponse;
import com.draftstd.hydraforge.application.response.sistema.ObterEntidadeSistemaQuery;
import java.util.UUID;

public interface GerenciarEntidadeSistemaUseCase {
    UUID criar(CriarEntidadeSistemaCommand command);
    void atualizar(AtualizarEntidadeSistemaCommand command);
    EntidadeSistemaQueryResponse obterPorId(ObterEntidadeSistemaQuery query);
}