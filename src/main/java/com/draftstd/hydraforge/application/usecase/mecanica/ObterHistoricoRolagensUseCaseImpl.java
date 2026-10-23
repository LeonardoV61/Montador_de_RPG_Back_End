package com.draftstd.hydraforge.application.usecase.mecanica;

import com.draftstd.hydraforge.application.response.mecanica.ObterHistoricoRolagensQuery;
import com.draftstd.hydraforge.application.response.mecanica.ResultadoRolagemQueryResponse;
import com.draftstd.hydraforge.ports.in.mecanica.ObterHistoricoRolagensUseCase;
import com.draftstd.hydraforge.ports.out.persistence.mecanica.ObterHistoricoRolagensPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ObterHistoricoRolagensUseCaseImpl implements ObterHistoricoRolagensUseCase {

    private static final int LIMITE_PADRAO = 50;
    private static final int LIMITE_MAXIMO = 200;

    private final ObterHistoricoRolagensPort obterHistoricoRolagensPort;

    public ObterHistoricoRolagensUseCaseImpl(ObterHistoricoRolagensPort obterHistoricoRolagensPort) {
        this.obterHistoricoRolagensPort = obterHistoricoRolagensPort;
    }

    @Override
    public List<ResultadoRolagemQueryResponse> executar(ObterHistoricoRolagensQuery query) {
        Integer pedido = query.limite();
        int limite = pedido == null ? LIMITE_PADRAO : Math.max(1, Math.min(pedido, LIMITE_MAXIMO));
        return obterHistoricoRolagensPort.buscarPorSessao(query.sessaoId(), query.personagemId(), limite);
    }
}