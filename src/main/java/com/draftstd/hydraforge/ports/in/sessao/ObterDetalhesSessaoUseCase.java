package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.query.sessao.ObterDetalhesSessaoQuery;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;

public interface ObterDetalhesSessaoUseCase {
    SessaoQueryResponse obterPorId(ObterDetalhesSessaoQuery query);
}