package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import com.draftstd.hydraforge.domain.events.DomainEvent; // Substituir pela classe concreta ex: CampanhaCriadaEvent
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.CriarCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.command.campanha.CriarCampanhaCommand;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.ports.out.campanha.SalvarCampanhaPort;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CriarCampanhaUseCaseImpl implements CriarCampanhaUseCase {

    private final SalvarCampanhaPort salvarCampanhaPort;
    private final EventStorePort eventStorePort;

    public CriarCampanhaUseCaseImpl(SalvarCampanhaPort salvarCampanhaPort, EventStorePort eventStorePort) {
        this.salvarCampanhaPort = salvarCampanhaPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public CampanhaQueryResponse executar(CriarCampanhaCommand command) {
        Campanha campanha = new Campanha();
        campanha.setId(UUID.randomUUID());
        campanha.setTitulo(command.titulo());
        campanha.setDescricao(command.descricao());
        campanha.setSistemaId(command.sistemaId());
        campanha.setMestreId(command.mestreId());
        campanha.setStatus(StatusCampanha.ATIVO);
        campanha.setCriadaEm(OffsetDateTime.now());

        Campanha salva = salvarCampanhaPort.salvar(campanha);

        DomainEvent eventoCriado = new DomainEvent() {};
        eventStorePort.saveEvents(salva.getId().toString(), List.of(eventoCriado), 0L);

        return new CampanhaQueryResponse(
                salva.getId(),
                salva.getTitulo(),
                salva.getDescricao(),
                salva.getSistemaId(),
                salva.getMestreId(),
                salva.getStatus(),
                salva.getCriadaEm()
        );
    }
}