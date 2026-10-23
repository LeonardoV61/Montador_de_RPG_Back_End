package com.draftstd.hydraforge.application.usecase.sessao.anotacao;

import com.draftstd.hydraforge.application.response.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.application.response.sessao.ObterAnotacaoQuery;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.ports.in.sessao.ObterAnotacaoUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.AnotacaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ObterAnotacaoUseCaseImpl implements ObterAnotacaoUseCase {

    private final AnotacaoPersistencePort anotacaoPersistencePort;

    public ObterAnotacaoUseCaseImpl(AnotacaoPersistencePort anotacaoPersistencePort) {
        this.anotacaoPersistencePort = anotacaoPersistencePort;
    }

    @Override
    public AnotacaoQueryResponse obterPorId(ObterAnotacaoQuery query) {
        Anotacao anotacao = anotacaoPersistencePort.buscarPorId(query.anotacaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anotação não encontrada: " + query.anotacaoId()));

        return new AnotacaoQueryResponse(
                anotacao.getId(),
                anotacao.getSessaoId(),
                anotacao.getUsuarioId(),
                anotacao.getTitulo(),
                anotacao.getConteudo(),
                anotacao.getEPrivado(),
                anotacao.getCriadaEm(),
                anotacao.getAtualizadaEm() 
        );
    }
}