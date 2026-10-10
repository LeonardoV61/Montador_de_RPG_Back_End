package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterDetalhesSessaoQuery;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.ObterDetalhesSessaoUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterSessaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ObterDetalhesSessaoUseCaseImpl implements ObterDetalhesSessaoUseCase {

    private final ObterSessaoPort obterSessaoPort;

    public ObterDetalhesSessaoUseCaseImpl(ObterSessaoPort obterSessaoPort) {
        this.obterSessaoPort = obterSessaoPort;
    }

    @Override
    public SessaoQueryResponse obterPorId(ObterDetalhesSessaoQuery query) {
        Sessao sessao = obterSessaoPort.buscarPorId(query.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + query.sessaoId()));

        return new SessaoQueryResponse(
                sessao.getId(),
                sessao.getCampanhaId(),
                sessao.getTitulo(),
                sessao.getDescricao(),
                sessao.getStatus(),
                sessao.getAgendadaPara(),
                sessao.getCriadaEm()
        );
    }
}