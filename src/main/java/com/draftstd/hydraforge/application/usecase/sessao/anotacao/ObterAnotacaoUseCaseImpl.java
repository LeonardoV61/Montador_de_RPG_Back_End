package com.draftstd.hydraforge.application.usecase.sessao.anotacao;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.ports.in.query.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterAnotacaoQuery;
import com.draftstd.hydraforge.ports.in.sessao.ObterAnotacaoUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterAnotacaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ObterAnotacaoUseCaseImpl implements ObterAnotacaoUseCase {

    private final ObterAnotacaoPort obterAnotacaoPort;

    public ObterAnotacaoUseCaseImpl(ObterAnotacaoPort obterAnotacaoPort) {
        this.obterAnotacaoPort = obterAnotacaoPort;
    }

    @Override
    public AnotacaoQueryResponse obterPorId(ObterAnotacaoQuery query) {
        Anotacao anotacao = obterAnotacaoPort.buscarPorId(query.anotacaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anotação não encontrada: " + query.anotacaoId()));

        return new AnotacaoQueryResponse(
                anotacao.getId(),
                anotacao.getSessaoId(),
                anotacao.getAutorId(),
                anotacao.getTitulo(),
                anotacao.getConteudo(),
                anotacao.isPrivada(),
                anotacao.getCriadaEm()
        );
    }
}