package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.domain.events.DomainEvent; // Ou evento concreto ex: ItemConsumidoEvent
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.command.personagem.UsarItemConsumivelCommand;
import com.draftstd.hydraforge.ports.in.personagem.UsarItemConsumivelUseCase;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.personagem.ObterPersonagemPort;
import com.draftstd.hydraforge.ports.out.personagem.SalvarPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsarItemConsumivelUseCaseImpl implements UsarItemConsumivelUseCase {

    private final ObterPersonagemPort obterPersonagemPort;
    private final SalvarPersonagemPort salvarPersonagemPort;
    private final EventStorePort eventStorePort;

    public UsarItemConsumivelUseCaseImpl(ObterPersonagemPort obterPersonagemPort,
                                         SalvarPersonagemPort salvarPersonagemPort,
                                         EventStorePort eventStorePort) {
        this.obterPersonagemPort = obterPersonagemPort;
        this.salvarPersonagemPort = salvarPersonagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public PersonagemQueryResponse executar(UsarItemConsumivelCommand command) {
        Personagem personagem = obterPersonagemPort.buscarPorId(command.personagemId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + command.personagemId()));

        // Lógica de remoção/uso do item consumível do inventário e aplicação de efeitos

        Personagem atualizado = salvarPersonagemPort.salvar(personagem);

        DomainEvent eventoItemUsado = new DomainEvent() {};
        eventStorePort.saveEvents(
                atualizado.getId().toString(),
                List.of(eventoItemUsado),
                atualizado.getVersao() != null ? atualizado.getVersao() : 1L
        );

        return new PersonagemQueryResponse(
                atualizado.getId(),
                atualizado.getUsuarioId(),
                atualizado.getCampanhaId(),
                atualizado.getNome(),
                atualizado.getAtributos()
        );
    }
}