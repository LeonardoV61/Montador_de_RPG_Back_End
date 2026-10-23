package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.application.request.sessao.CriarSessaoCommand;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.sessao.CriarSessaoUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CriarSessaoUseCaseImpl implements CriarSessaoUseCase {

    private final SessaoPersistencePort sessaoPersistencePort;
    private final EventStorePort eventStorePort;

    public CriarSessaoUseCaseImpl(SessaoPersistencePort sessaoPersistencePort, EventStorePort eventStorePort) {
        this.sessaoPersistencePort = sessaoPersistencePort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    @Transactional
    public UUID executar(CriarSessaoCommand command) {
        UUID novoId = UUID.randomUUID();

        Sessao sessao = new Sessao(
                novoId,
                command.titulo(),
                command.campanhaId(),
                command.ordem()
        );

        var eventos = sessao.pullEventos();
        eventStorePort.salvarEventos(sessao.getId().toString(), eventos, sessao.getVersion());

        return novoId;
    }
}