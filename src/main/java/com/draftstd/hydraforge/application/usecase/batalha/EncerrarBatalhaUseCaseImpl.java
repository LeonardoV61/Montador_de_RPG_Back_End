package com.draftstd.hydraforge.application.usecase.batalha;

import com.draftstd.hydraforge.domain.enums.BatalhaStatus;
import com.draftstd.hydraforge.domain.events.batalha.BatalhaEncerradaEvent;
import com.draftstd.hydraforge.domain.events.batalha.BatalhaIniciadaEvent;
import com.draftstd.hydraforge.domain.events.batalha.ParticipanteBatalhaAlteradoEvent;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.domain.model.batalha.BatalhaParticipantes;
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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

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

        batalha.setStatus(BatalhaStatus.CONCLUIDA);
        Batalha encerrada = batalhaOutputPort.salvar(batalha);

        long proximaVersao = encerrada.getVersao() != null ? encerrada.getVersao() + 1 : 1L;
        BatalhaEncerradaEvent eventoEncerrada = new BatalhaEncerradaEvent(
                UUID.randomUUID(), encerrada.getId().toString(), proximaVersao, Instant.now()
        );
        eventStorePort.saveEvents(encerrada.getId().toString(), List.of(eventoEncerrada), proximaVersao - 1);

        final List<BatalhaQueryResponse.ParticipanteBatalhaResponse> participantesResponse = (encerrada.getParticipantes() != null) ? encerrada.getParticipantes().stream().map(p -> new BatalhaQueryResponse.ParticipanteBatalhaResponse(
                p.getEntidadeInstancia() != null ? p.getEntidadeInstancia().getId() : null,
                p.getEntidadeInstancia() == null ? p.getEntidadeInstancia().getNome() : "Desconhecido",
                p.getEntidadeInstancia() != null ? p.getEntidadeInstancia().getTipo() : "PADRAO",
                p.getOrdemIniciativa(),
                p.isAtivo()
            )).toList() : List.of();

        return new BatalhaQueryResponse(encerrada.getId(), encerrada.getSessaoId(), encerrada.getCenaId(), encerrada.getStatus().name(), encerrada.getRodadaAtual(), encerrada.getIniciadaEm(), participantesResponse);
    }
}
