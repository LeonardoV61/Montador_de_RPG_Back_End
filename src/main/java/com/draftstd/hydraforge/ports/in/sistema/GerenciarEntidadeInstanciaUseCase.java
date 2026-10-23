package com.draftstd.hydraforge.ports.in.sistema;

import com.draftstd.hydraforge.application.request.sistema.AtualizarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.application.request.sistema.CriarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.application.response.sistema.EntidadeInstanciaQueryResponse;
import com.draftstd.hydraforge.application.response.sistema.ObterEntidadeInstanciaQuery;
import java.util.UUID;

public interface GerenciarEntidadeInstanciaUseCase {
    UUID criar(CriarEntidadeInstanciaCommand command);
    void atualizar(AtualizarEntidadeInstanciaCommand command);
    EntidadeInstanciaQueryResponse obterPorId(ObterEntidadeInstanciaQuery query);
}