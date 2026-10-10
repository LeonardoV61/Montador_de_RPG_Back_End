package com.draftstd.hydraforge.application.usecase.sessao.cena;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.ports.in.query.sessao.CenaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterCenaQuery;
import com.draftstd.hydraforge.ports.in.sessao.ObterCenaUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterCenaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ObterCenaUseCaseImpl implements ObterCenaUseCase {

    private final ObterCenaPort obterCenaPort;

    public ObterCenaUseCaseImpl(ObterCenaPort obterCenaPort) {
        this.obterCenaPort = obterCenaPort;
    }

    @Override
    public CenaQueryResponse obterPorId(ObterCenaQuery query) {
        Cena cena = obterCenaPort.buscarPorId(query.cenaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cena não encontrada: " + query.cenaId()));

        return new CenaQueryResponse(
                cena.getId(),
                cena.getSessaoId(),
                cena.getTitulo(),
                cena.getAmbiente(),
                cena.getMapaUrl(),
                cena.isAtiva()
        );
    }
}