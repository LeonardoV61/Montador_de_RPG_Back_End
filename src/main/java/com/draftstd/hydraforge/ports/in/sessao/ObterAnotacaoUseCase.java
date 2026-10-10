package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.query.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterAnotacaoQuery;

public interface ObterAnotacaoUseCase {
    AnotacaoQueryResponse obterPorId(ObterAnotacaoQuery query);
}