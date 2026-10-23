package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.application.response.sessao.ObterDetalhesSessaoQuery;
import com.draftstd.hydraforge.application.response.sessao.SessaoQueryResponse;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.sessao.ObterDetalhesSessaoUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ObterDetalhesSessaoUseCaseImpl implements ObterDetalhesSessaoUseCase {

    private final SessaoPersistencePort sessaoPersistencePort;

    public ObterDetalhesSessaoUseCaseImpl(SessaoPersistencePort sessaoPersistencePort) {
        this.sessaoPersistencePort = sessaoPersistencePort;
    }

    @Override
    public SessaoQueryResponse obterPorId(ObterDetalhesSessaoQuery query) {
        Sessao sessao = sessaoPersistencePort.buscarPorId(query.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + query.sessaoId()));

        return new SessaoQueryResponse(
                sessao.getId(),
                sessao.getCampanhaId(),
                sessao.getTitulo(),
                sessao.getStatus(),
                sessao.getOrdem(),
                sessao.getDataInicio(),
                sessao.getDataFim(),
                sessao.getProcedimentoAtivoId(),
                List.of()
        );
    }
}