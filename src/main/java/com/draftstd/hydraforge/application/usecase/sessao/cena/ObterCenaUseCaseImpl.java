package com.draftstd.hydraforge.application.usecase.sessao.cena;

import com.draftstd.hydraforge.application.response.sessao.CenaQueryResponse;
import com.draftstd.hydraforge.application.response.sessao.ObterCenaQuery;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.ports.in.sessao.ObterCenaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ObterCenaUseCaseImpl implements ObterCenaUseCase {

    private final CenaPersistencePort cenaPersistencePort;

    public ObterCenaUseCaseImpl(CenaPersistencePort cenaPersistencePort) {
        this.cenaPersistencePort = cenaPersistencePort;
    }

    @Override
    public CenaQueryResponse obterPorId(ObterCenaQuery query) {
        Cena cena = cenaPersistencePort.buscarPorId(query.cenaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cena não encontrada: " + query.cenaId()));

        return new CenaQueryResponse(
                cena.getId(),
                cena.getSessaoId(),        // Retorna o ID da sessão associada
                "Cena " + cena.getOrdem(), // Título mapeado/gerado a partir da ordem
                cena.getTipo(),            // Ambiente
                cena.getUrlMapa(),         // MapaUrl
                true,                      // Ativa
                cena.getCriadaEm()         // Data de Criação real
        );
    }
}