package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarEntidadeSistemaCommand;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarEntidadeSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.EntidadeSistemaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sistema.ObterEntidadeSistemaQuery;

public interface GerenciarEntidadeSistemaUseCase {
    EntidadeSistemaQueryResponse criar(CriarEntidadeSistemaCommand command);
    EntidadeSistemaQueryResponse atualizar(AtualizarEntidadeSistemaCommand command);
    EntidadeSistemaQueryResponse obterPorId(ObterEntidadeSistemaQuery query);
}