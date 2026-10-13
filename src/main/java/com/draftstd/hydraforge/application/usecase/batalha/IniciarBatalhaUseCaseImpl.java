package com.draftstd.hydraforge.application.usecase.batalha;

import com.draftstd.hydraforge.domain.enums.BatalhaStatus;
import com.draftstd.hydraforge.domain.events.batalha.BatalhaEncerradaEvent;
import com.draftstd.hydraforge.domain.events.batalha.BatalhaIniciadaEvent;
import com.draftstd.hydraforge.domain.events.batalha.ParticipanteBatalhaAlteradoEvent;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.ports.in.batalha.EncerrarBatalhaUseCase;
import com.draftstd.hydraforge.ports.in.batalha.GerenciarParticipantesBatalhaUseCase;
import com.draftstd.hydraforge.ports.in.batalha.IniciarBatalhaUseCase;
import com.draftstd.hydraforge.ports.in.command.batalha.AdicionarParticipanteBatalhaCommand;
import com.draftstd.hydraforge.ports.in.command.batalha.EncerrarBatalhaCommand;
import com.draftstd.hydraforge.ports.in.command.batalha.IniciarBatalhaCommand;
import com.draftstd.hydraforge.ports.in.command.batalha.RemoverParticipanteBatalhaCommand;
import com.draftstd.hydraforge.ports.in.query.batalha.BatalhaQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.batalha.BatalhaOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
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
        batalha.setIniciadaEm(LocalDateTime.now());

        Batalha salva = batalhaOutputPort.salvar(batalha);

        BatalhaIniciadaEvent eventoIniciada = new BatalhaIniciadaEvent(
                UUID.randomUUID(),
                salva.getId().toString(),
                command.sessaoId(),
                command.cenaId(),
                1L,
                Instant.now()
        );
        eventStorePort.saveEvents(salva.getId().toString(), List.of(eventoIniciada), 0L);

        return new BatalhaQueryResponse(
            salva.getId(), salva.getSessaoId(), salva.getCenaId(), salva.getStatus().name(), salva.getRodadaAtual(), salva.getIniciadaEm(), 
            salva.getParticipantes().stream().map(p -> new BatalhaQueryResponse.ParticipanteBatalhaResponse(
                p.getEntidadeInstancia() != null ? p.getEntidadeInstancia().getId() : null,
                p.getEntidadeInstancia() != null ? p.getEntidadeInstancia().getNome() : "Desconhecido",
                p.getEntidadeInstancia() != null ? p.getEntidadeInstancia().getTipo() : "PADRAO",
                p.getOrdemIniciativa(),
                p.isAtivo()
            )
        ).toList());
    }
}


