package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.domain.events.DomainEvent; // Ou evento concreto ex: DescansoRealizadoEvent
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.command.personagem.RealizarDescansoCommand;
import com.draftstd.hydraforge.ports.in.personagem.RealizarDescansoUseCase;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.personagem.ObterPersonagemPort;
import com.draftstd.hydraforge.ports.out.personagem.SalvarPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RealizarDescansoUseCaseImpl implements RealizarDescansoUseCase {

    private final ObterPersonagemPort obterPersonagemPort;
    private final SalvarPersonagemPort salvarPersonagemPort;
    private final EventStorePort eventStorePort;

    public RealizarDescansoUseCaseImpl(ObterPersonagemPort obterPersonagemPort,
                                       SalvarPersonagemPort salvarPersonagemPort,
                                       EventStorePort eventStorePort) {
        this.obterPersonagemPort = obterPersonagemPort;
        this.salvarPersonagemPort = salvarPersonagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public PersonagemQueryResponse executar(RealizarDescansoCommand command) {
        Personagem personagem = obterPersonagemPort.buscarPorId(command.personagemId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + command.personagemId()));

        // Lógica de restauração de PV/Recursos conforme tipo de descanso (Curto/Longo)
        Personagem atualizado = salvarPersonagemPort.salvar(personagem);

        DomainEvent eventoDescanso = new DomainEvent() {};
        eventStorePort.saveEvents(
                atualizado.getId().toString(),
                List.of(eventoDescanso),
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