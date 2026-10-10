package com.draftstd.hydraforge.application.usecase.batalha;

import com.draftstd.hydraforge.domain.events.DomainEvent; // Ou eventos concretos ex: ParticipanteAdicionadoBatalhaEvent / ParticipanteRemovidoBatalhaEvent
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.ports.in.batalha.GerenciarParticipantesBatalhaUseCase;
import com.draftstd.hydraforge.ports.in.command.batalha.AdicionarParticipanteBatalhaCommand;
import com.draftstd.hydraforge.ports.in.command.batalha.RemoverParticipanteBatalhaCommand;
import com.draftstd.hydraforge.ports.in.query.batalha.BatalhaQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.batalha.BatalhaOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        // Lógica de inclusão do participante e reordenação da fila de iniciativa

        Batalha atualizada = batalhaOutputPort.salvar(batalha);

        DomainEvent eventoAdicionado = new DomainEvent() {};
        eventStorePort.saveEvents(
                atualizada.getId().toString(),
                List.of(eventoAdicionado),
                atualizada.getVersao() != null ? atualizada.getVersao() : 1L
        );

        return new BatalhaQueryResponse(
                atualizada.getId(),
                atualizada.getSessaoId(),
                atualizada.getCenaId(),
                atualizada.getStatus().name(),
                atualizada.getRodadaAtual(),
                atualizada.getIniciadaEm()
        );
    }

    @Override
    public BatalhaQueryResponse removerParticipante(RemoverParticipanteBatalhaCommand command) {
        Batalha batalha = batalhaOutputPort.buscarPorId(command.batalhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Batalha não encontrada: " + command.batalhaId()));

        // Lógica de remoção do participante da batalha ativa

        Batalha atualizada = batalhaOutputPort.salvar(batalha);

        DomainEvent eventoRemovido = new DomainEvent() {};
        eventStorePort.saveEvents(
                atualizada.getId().toString(),
                List.of(eventoRemovido),
                atualizada.getVersao() != null ? atualizada.getVersao() : 1L
        );

        return new BatalhaQueryResponse(
                atualizada.getId(),
                atualizada.getSessaoId(),
                atualizada.getCenaId(),
                atualizada.getStatus().name(),
                atualizada.getRodadaAtual(),
                atualizada.getIniciadaEm()
        );
    }
}