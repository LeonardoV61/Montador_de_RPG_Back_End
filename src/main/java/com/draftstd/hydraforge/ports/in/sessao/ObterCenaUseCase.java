package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.ports.in.query.sessao.CenaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterCenaQuery;

public interface ObterCenaUseCase {
    CenaQueryResponse obterPorId(ObterCenaQuery query);
}