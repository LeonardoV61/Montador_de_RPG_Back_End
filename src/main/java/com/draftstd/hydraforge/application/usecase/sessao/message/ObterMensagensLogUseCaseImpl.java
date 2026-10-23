package com.draftstd.hydraforge.application.usecase.sessao.message;

import com.draftstd.hydraforge.application.response.sessao.MensagemLogQueryResponse;
import com.draftstd.hydraforge.application.response.sessao.ObterMensagensLogQuery;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.ports.in.sessao.ObterMensagensLogUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.MensagemLogPersistencePort;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ObterMensagensLogUseCaseImpl implements ObterMensagensLogUseCase {

    private final MensagemLogPersistencePort mensagemLogPersistencePort;
    private final SessaoPersistencePort sessaoPersistencePort;

    public ObterMensagensLogUseCaseImpl(MensagemLogPersistencePort mensagemLogPersistencePort, SessaoPersistencePort sessaoPersistencePort) {
        this.mensagemLogPersistencePort = mensagemLogPersistencePort;
        this.sessaoPersistencePort = sessaoPersistencePort;
    }

    @Override
    public List<MensagemLogQueryResponse> listarPorSessao(ObterMensagensLogQuery query) {
        sessaoPersistencePort.buscarPorId(query.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + query.sessaoId()));

        return mensagemLogPersistencePort.listarPorSessaoId(query.sessaoId()).stream()
                .limit(query.limite())
                .map(msg -> new MensagemLogQueryResponse(
                        msg.id(),
                        query.sessaoId(),
                        msg.usuarioId(),
                        msg.conteudo(),
                        msg.momento()
                ))
                .toList();
    }
}