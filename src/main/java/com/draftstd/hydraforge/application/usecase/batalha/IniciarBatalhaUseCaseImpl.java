package com.draftstd.hydraforge.application.usecase.batalha;

import com.draftstd.hydraforge.domain.events.batalha.BatalhaIniciadaEvent;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.ports.in.batalha.IniciarBatalhaUseCase;
import com.draftstd.hydraforge.application.request.batalha.IniciarBatalhaCommand;
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
        // Se a entidade Batalha possuir cenaId no seu modelo, atribua aqui (conforme command.cenaId())
        batalha.iniciarBatalha();

        Batalha salva = batalhaOutputPort.salvar(batalha);

        BatalhaIniciadaEvent eventoIniciada = new BatalhaIniciadaEvent(
                UUID.randomUUID(),
                salva.getId().toString(),
                command.sessaoId(),
                command.cenaId(),
                1L,
                Instant.now()
        );
        eventStorePort.salvarEventos(salva.getId().toString(), List.of(eventoIniciada), 0L);

        return new BatalhaQueryResponse(
            salva.getId(), 
            salva.getSessaoId(), 
            salva.getCampanhaId(), // ou command.cenaId() dependendo do construtor do Response
            salva.getStatus().name(), 
            salva.getRodadaAtual(), 
            salva.getInicioEm(), // Ajustado para getInicioEm()
            salva.getParticipantes().stream().map(p -> new BatalhaQueryResponse.ParticipanteBatalhaResponse(
                p.getInstanciaId(), // Utiliza instanciaId
                "Instância " + p.getInstanciaId(),
                "PADRAO",
                p.getOrdemIniciativa(),
                p.isAtivo()
            )).toList()
        );
    }
}