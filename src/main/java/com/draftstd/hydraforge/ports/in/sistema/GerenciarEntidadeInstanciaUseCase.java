package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.EntidadeInstanciaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sistema.ObterEntidadeInstanciaQuery;

public interface GerenciarEntidadeInstanciaUseCase {
    EntidadeInstanciaQueryResponse criar(CriarEntidadeInstanciaCommand command);
    EntidadeInstanciaQueryResponse atualizar(AtualizarEntidadeInstanciaCommand command);
    EntidadeInstanciaQueryResponse obterPorId(ObterEntidadeInstanciaQuery query);
}