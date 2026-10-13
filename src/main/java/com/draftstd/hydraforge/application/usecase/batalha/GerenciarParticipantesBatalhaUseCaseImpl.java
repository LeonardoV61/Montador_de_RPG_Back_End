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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GerenciarParticipantesBatalhaUseCaseImpl implements GerenciarParticipantesBatalhaUseCase {

    private final BatalhaOutputPort batalhaOutputPort;
    private final EventStorePort eventStorePort;

    public GerenciarParticipantesBatalhaUseCaseImpl(BatalhaOutputPort batalhaOutputPort, EventStorePort eventStorePort) {
        this.batalhaOutputPort = batalhaOutputPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public BatalhaQueryResponse adicionarParticipante(AdicionarParticipanteBatalhaCommand command) {
        Batalha batalha = batalhaOutputPort.buscarPorId(command.batalhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Batalha não encontrada: " + command.batalhaId()));

        Batalha atualizada = batalhaOutputPort.salvar(batalha);
        long versao = atualizada.getVersao() != null ? atualizada.getVersao() + 1 : 1L;

        ParticipanteBatalhaAlteradoEvent evento = new ParticipanteBatalhaAlteradoEvent(
                UUID.randomUUID(), atualizada.getId().toString(), command.entidadeInstanciaId(), "ADICIONAR", versao, Instant.now()
        );
        eventStorePort.saveEvents(atualizada.getId().toString(), List.of(evento), versao - 1);

        return toQueryResponse(atualizada);
    }

    @Override
    public BatalhaQueryResponse removerParticipante(RemoverParticipanteBatalhaCommand command) {
        Batalha batalha = batalhaOutputPort.buscarPorId(command.batalhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Batalha não encontrada: " + command.batalhaId()));

        Batalha atualizada = batalhaOutputPort.salvar(batalha);
        long versao = atualizada.getVersao() != null ? atualizada.getVersao() + 1 : 1L;

        ParticipanteBatalhaAlteradoEvent evento = new ParticipanteBatalhaAlteradoEvent(
                UUID.randomUUID(), atualizada.getId().toString(), command.entidadeInstanciaId(), "REMOVER", versao, Instant.now()
        );
        eventStorePort.saveEvents(atualizada.getId().toString(), List.of(evento), versao - 1);

        return toQueryResponse(atualizada);
    }

    private BatalhaQueryResponse toQueryResponse(Batalha b) {
        return new BatalhaQueryResponse(b.getId(), b.getSessaoId(), b.getCenaId(), b.getStatus().name(), b.getRodadaAtual(), b.getIniciadaEm(), b.getParticipantes().stream().map(p -> new BatalhaQueryResponse.ParticipanteBatalhaResponse(
                p.getEntidadeInstancia() != null ? p.getEntidadeInstancia().getId() : null,
                p.getEntidadeInstancia() != null ? p.getEntidadeInstancia().getNome() : "Desconhecido",
                p.getEntidadeInstancia() != null ? p.getEntidadeInstancia().getTipo() : "PADRAO",
                p.getOrdemIniciativa(),
                p.isAtivo()
        )).toList());
    }
}