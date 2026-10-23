package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.application.request.sessao.EntrarSessaoCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarParticipantesSessaoUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GerenciarParticipantesSessaoUseCaseImpl implements GerenciarParticipantesSessaoUseCase {

    private final SessaoPersistencePort sessaoPersistencePort;

    public GerenciarParticipantesSessaoUseCaseImpl(SessaoPersistencePort sessaoPersistencePort) {
        this.sessaoPersistencePort = sessaoPersistencePort;
    }

    @Override
    public void entrarSessao(EntrarSessaoCommand command) {
        sessaoPersistencePort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        // Mutações e registos adicionais do participante na sessão via agregados/portas de suporte
    }
}