package com.draftstd.hydraforge.application.usecase.batalha;

import com.draftstd.hydraforge.domain.enums.BatalhaStatus;
import com.draftstd.hydraforge.domain.events.DomainEvent; // Ou evento concreto ex: BatalhaEncerradaEvent
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.ports.in.batalha.EncerrarBatalhaUseCase;
import com.draftstd.hydraforge.ports.in.command.batalha.EncerrarBatalhaCommand;
import com.draftstd.hydraforge.ports.in.query.batalha.BatalhaQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.batalha.BatalhaOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EncerrarBatalhaUseCaseImpl implements EncerrarBatalhaUseCase {

    private final BatalhaOutputPort batalhaOutputPort;
    private final EventStorePort eventStorePort;

    public EncerrarBatalhaUseCaseImpl(BatalhaOutputPort batalhaOutputPort, EventStorePort eventStorePort) {
        this.batalhaOutputPort = batalhaOutputPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public BatalhaQueryResponse executar(EncerrarBatalhaCommand command) {
        Batalha batalha = batalhaOutputPort.buscarPorId(command.batalhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Batalha não encontrada: " + command.batalhaId()));

        batalha.setStatus(BatalhaStatus.FINALIZADA);

        Batalha encerrada = batalhaOutputPort.salvar(batalha);

        // Registro do evento de encerramento na EventStore
        DomainEvent eventoEncerrada = new DomainEvent() {}; // Pode ser substituído pela classe concreta BatalhaEncerradaEvent
        eventStorePort.saveEvents(
                encerrada.getId().toString(),
                List.of(eventoEncerrada),
                encerrada.getVersao() != null ? encerrada.getVersao() : 1L
        );

        return new BatalhaQueryResponse(
                encerrada.getId(),
                encerrada.getSessaoId(),
                encerrada.getCenaId(),
                encerrada.getStatus().name(),
                encerrada.getRodadaAtual(),
                encerrada.getIniciadaEm()
        );
    }
}