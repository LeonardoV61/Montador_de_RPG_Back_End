package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.application.request.sessao.AlterarStatusSessaoCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarStatusSessaoUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GerenciarStatusSessaoUseCaseImpl implements GerenciarStatusSessaoUseCase {

    private final SessaoPersistencePort sessaoPersistencePort;
    private final EventStorePort eventStorePort;

    public GerenciarStatusSessaoUseCaseImpl(SessaoPersistencePort sessaoPersistencePort, EventStorePort eventStorePort) {
        this.sessaoPersistencePort = sessaoPersistencePort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public void alterarStatus(AlterarStatusSessaoCommand command) {
        Sessao sessao = sessaoPersistencePort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        switch (command.novoStatus()) {
            case ATIVA -> sessao.iniciarSessao();
            case PAUSADA -> sessao.pausarSessao();
            case ENCERRADA -> sessao.encerrarSessao(command.snapshotFinalJson());
            default -> throw new IllegalArgumentException("Transição de status inválida: " + command.novoStatus());
        }

        sessaoPersistencePort.salvar(sessao);
        eventStorePort.salvarEventos(sessao.getId().toString(), sessao.pullEventos(), sessao.getVersion());
    }
}