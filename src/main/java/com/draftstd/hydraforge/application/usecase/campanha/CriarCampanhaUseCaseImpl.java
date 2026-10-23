package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.application.request.campanha.CriarCampanhaCommand;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.CriarCampanhaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.campanha.CampanhaPersistencePort;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CriarCampanhaUseCaseImpl implements CriarCampanhaUseCase {

    private final CampanhaPersistencePort campanhaPersistencePort;
    private final EventStorePort eventStorePort;

    public CriarCampanhaUseCaseImpl(CampanhaPersistencePort campanhaPersistencePort, EventStorePort eventStorePort) {
        this.campanhaPersistencePort = campanhaPersistencePort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    @Transactional
    public UUID executar(CriarCampanhaCommand command) {
        UUID novoId = UUID.randomUUID();
        Campanha campanha = new Campanha(
            novoId,
            command.nome(),
            command.descricao(),
            command.urlImagem(),
            command.sistemaId(),
            command.mestreId()
        );

        campanhaPersistencePort.salvar(campanha);
        eventStorePort.salvarEventos(campanha.getId().toString(), campanha.pullEventos(), campanha.getVersion());

        return novoId;
    }
}