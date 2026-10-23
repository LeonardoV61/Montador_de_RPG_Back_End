package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.response.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.application.response.sessao.ObterAnotacaoQuery;

public interface ObterAnotacaoUseCase {
    AnotacaoQueryResponse obterPorId(ObterAnotacaoQuery query);
}