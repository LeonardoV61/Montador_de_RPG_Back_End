package com.draftstd.hydraforge.application.usecase.mecanica;

import com.draftstd.hydraforge.domain.events.DomainEvent; // Ou evento concreto ex: ResolucaoProcessadaEvent
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.mecanica.Resolucao;
import com.draftstd.hydraforge.ports.in.mecanica.GerenciarResolucaoUseCase;
import com.draftstd.hydraforge.ports.in.command.mecanica.RolarDadosCommand;
import com.draftstd.hydraforge.ports.in.query.mecanica.ResultadoRolagemQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.mecanica.ResolucaoOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GerenciarResolucaoUseCaseImpl implements GerenciarResolucaoUseCase {

    private final ResolucaoOutputPort resolucaoOutputPort;
    private final EventStorePort eventStorePort;

    public GerenciarResolucaoUseCaseImpl(ResolucaoOutputPort resolucaoOutputPort,
                                        EventStorePort eventStorePort) {
        this.resolucaoOutputPort = resolucaoOutputPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public ResultadoRolagemQueryResponse processarResolucao(RolarDadosCommand command) {
        Resolucao resolucao = new Resolucao();
        resolucao.setId(UUID.randomUUID());
        resolucao.setPersonagemId(command.personagemId());
        resolucao.setFormula(command.formula());

        Resolucao salva = resolucaoOutputPort.salvar(resolucao);

        DomainEvent eventoResolucao = new DomainEvent() {};
        eventStorePort.saveEvents(salva.getId().toString(), List.of(eventoResolucao), 0L);

        return new ResultadoRolagemQueryResponse(
                salva.getId(),
                salva.getPersonagemId(),
                salva.getFormula(),
                salva.getResultadoTotal(),
                salva.getDetalhes()
        );
    }
}