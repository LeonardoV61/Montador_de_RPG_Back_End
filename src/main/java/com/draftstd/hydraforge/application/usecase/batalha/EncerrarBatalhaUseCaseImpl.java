package com.draftstd.hydraforge.application.usecase.batalha;

import com.draftstd.hydraforge.domain.events.batalha.BatalhaEncerradaEvent;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.ports.in.batalha.EncerrarBatalhaUseCase;
import com.draftstd.hydraforge.application.request.batalha.EncerrarBatalhaCommand;
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

        batalha.finalizarBatalha();
        Batalha encerrada = batalhaOutputPort.salvar(batalha);

        long versao = 1L;
        BatalhaEncerradaEvent eventoEncerrada = new BatalhaEncerradaEvent(
                UUID.randomUUID(), encerrada.getId().toString(), versao, Instant.now()
        );
        eventStorePort.salvarEventos(encerrada.getId().toString(), List.of(eventoEncerrada), 0L);

        final List<BatalhaQueryResponse.ParticipanteBatalhaResponse> participantesResponse = 
            (encerrada.getParticipantes() != null) ? encerrada.getParticipantes().stream().map(p -> 
                new BatalhaQueryResponse.ParticipanteBatalhaResponse(
                    p.getInstanciaId(),
                    "Instância " + p.getInstanciaId(),
                    "PADRAO",
                    p.getOrdemIniciativa(),
                    p.isAtivo()
                )
            ).toList() : List.of();

        return new BatalhaQueryResponse(
            encerrada.getId(), 
            encerrada.getSessaoId(), 
            encerrada.getCampanhaId(), // Utiliza campanhaId em vez de getCenaId()
            encerrada.getStatus().name(), 
            encerrada.getRodadaAtual(), 
            encerrada.getInicioEm(), 
            participantesResponse
        );
    }
}