package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.response.sessao.ObterDetalhesSessaoQuery;
import com.draftstd.hydraforge.application.response.sessao.SessaoQueryResponse;

public interface ObterDetalhesSessaoUseCase {
    SessaoQueryResponse obterPorId(ObterDetalhesSessaoQuery query);
}