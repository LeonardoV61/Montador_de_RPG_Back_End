package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.domain.events.DomainEvent; // Ou evento concreto ex: PersonagemDeletadoEvent
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.command.personagem.DeletarPersonagemCommand;
import com.draftstd.hydraforge.ports.in.personagem.DeletarPersonagemUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.personagem.DeletarPersonagemPort;
import com.draftstd.hydraforge.ports.out.personagem.ObterPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeletarPersonagemUseCaseImpl implements DeletarPersonagemUseCase {

    private final ObterPersonagemPort obterPersonagemPort;
    private final DeletarPersonagemPort deletarPersonagemPort;
    private final EventStorePort eventStorePort;

    public DeletarPersonagemUseCaseImpl(ObterPersonagemPort obterPersonagemPort,
                                        DeletarPersonagemPort deletarPersonagemPort,
                                        EventStorePort eventStorePort) {
        this.obterPersonagemPort = obterPersonagemPort;
        this.deletarPersonagemPort = deletarPersonagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public void executar(DeletarPersonagemCommand command) {
        Personagem personagem = obterPersonagemPort.buscarPorId(command.personagemId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + command.personagemId()));

        DomainEvent eventoDeletado = new DomainEvent() {};
        eventStorePort.saveEvents(
                personagem.getId().toString(),
                List.of(eventoDeletado),
                personagem.getVersao() != null ? personagem.getVersao() : 1L
        );

        deletarPersonagemPort.deletarPorId(command.personagemId());
    }
}