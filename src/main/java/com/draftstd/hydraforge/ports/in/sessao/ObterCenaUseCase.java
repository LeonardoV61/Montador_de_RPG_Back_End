package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.response.sessao.CenaQueryResponse;
import com.draftstd.hydraforge.application.response.sessao.ObterCenaQuery;

public interface ObterCenaUseCase {
    CenaQueryResponse obterPorId(ObterCenaQuery query);
}