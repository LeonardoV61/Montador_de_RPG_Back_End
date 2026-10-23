package com.draftstd.hydraforge.application.usecase.batalha;

import com.draftstd.hydraforge.domain.events.batalha.ParticipanteBatalhaAlteradoEvent;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.ports.in.batalha.GerenciarParticipantesBatalhaUseCase;
import com.draftstd.hydraforge.application.request.batalha.AdicionarParticipanteBatalhaCommand;
import com.draftstd.hydraforge.application.request.batalha.RemoverParticipanteBatalhaCommand;
import com.draftstd.hydraforge.application.response.batalha.BatalhaQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.batalha.BatalhaOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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

        // Lógica de adição de participante ao domínio (se aplicável na entidade Batalha)
        Batalha atualizada = batalhaOutputPort.salvar(batalha);
        long versao = 1L;

        ParticipanteBatalhaAlteradoEvent evento = new ParticipanteBatalhaAlteradoEvent(
                UUID.randomUUID(), atualizada.getId().toString(), command.entidadeInstanciaId(), "ADICIONAR", versao, Instant.now()
        );
        eventStorePort.salvarEventos(atualizada.getId().toString(), List.of(evento), 0L);

        return toQueryResponse(atualizada);
    }

    @Override
    public BatalhaQueryResponse removerParticipante(RemoverParticipanteBatalhaCommand command) {
        Batalha batalha = batalhaOutputPort.buscarPorId(command.batalhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Batalha não encontrada: " + command.batalhaId()));

        // Lógica de remoção de participante ao domínio (se aplicável na entidade Batalha)
        Batalha atualizada = batalhaOutputPort.salvar(batalha);
        long versao = 1L;

        ParticipanteBatalhaAlteradoEvent evento = new ParticipanteBatalhaAlteradoEvent(
                UUID.randomUUID(), atualizada.getId().toString(), command.entidadeInstanciaId(), "REMOVER", versao, Instant.now()
        );
        eventStorePort.salvarEventos(atualizada.getId().toString(), List.of(evento), 0L);

        return toQueryResponse(atualizada);
    }

    private BatalhaQueryResponse toQueryResponse(Batalha b) {
        return new BatalhaQueryResponse(
            b.getId(), 
            b.getSessaoId(), 
            b.getCampanhaId(), // Utiliza campanhaId em vez de getCenaId()
            b.getStatus().name(), 
            b.getRodadaAtual(), 
            b.getInicioEm(), 
            b.getParticipantes().stream().map(p -> new BatalhaQueryResponse.ParticipanteBatalhaResponse(
                p.getInstanciaId(),
                "Instância " + p.getInstanciaId(),
                "PADRAO",
                p.getOrdemIniciativa(),
                p.isAtivo()
            )).toList()
        );
    }
}