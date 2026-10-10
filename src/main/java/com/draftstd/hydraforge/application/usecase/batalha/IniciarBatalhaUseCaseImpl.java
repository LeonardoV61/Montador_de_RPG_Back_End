package com.draftstd.hydraforge.application.usecase.batalha;

import com.draftstd.hydraforge.domain.enums.BatalhaStatus;
import com.draftstd.hydraforge.domain.events.DomainEvent; // Ou evento específico ex: BatalhaIniciadaEvent
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.ports.in.batalha.IniciarBatalhaUseCase;
import com.draftstd.hydraforge.ports.in.command.batalha.IniciarBatalhaCommand;
import com.draftstd.hydraforge.ports.in.query.batalha.BatalhaQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.batalha.BatalhaOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IniciarBatalhaUseCaseImpl implements IniciarBatalhaUseCase {

    private final BatalhaOutputPort batalhaOutputPort;
    private final EventStorePort eventStorePort;

    public IniciarBatalhaUseCaseImpl(BatalhaOutputPort batalhaOutputPort, EventStorePort eventStorePort) {
        this.batalhaOutputPort = batalhaOutputPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public BatalhaQueryResponse executar(IniciarBatalhaCommand command) {
        Batalha batalha = new Batalha();
        batalha.setId(UUID.randomUUID());
        batalha.setSessaoId(command.sessaoId());
        batalha.setCenaId(command.cenaId());
        batalha.setStatus(BatalhaStatus.EM_ANDAMENTO);
        batalha.setRodadaAtual(1);
        batalha.setIniciadaEm(OffsetDateTime.now());

        Batalha salva = batalhaOutputPort.salvar(batalha);

        // Registro do evento de início de batalha na EventStore
        DomainEvent eventoIniciada = new DomainEvent() {}; // Pode ser substituído pela classe concreta BatalhaIniciadaEvent
        eventStorePort.saveEvents(salva.getId().toString(), List.of(eventoIniciada), 0L);

        return new BatalhaQueryResponse(
                salva.getId(),
                salva.getSessaoId(),
                salva.getCenaId(),
                salva.getStatus().name(),
                salva.getRodadaAtual(),
                salva.getIniciadaEm()
        );
    }
}