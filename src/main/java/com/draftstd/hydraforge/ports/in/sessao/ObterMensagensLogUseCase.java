package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.response.sessao.MensagemLogQueryResponse;
import com.draftstd.hydraforge.application.response.sessao.ObterMensagensLogQuery;

import java.util.List;

public interface ObterMensagensLogUseCase {
    List<MensagemLogQueryResponse> listarPorSessao(ObterMensagensLogQuery query);
}