package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.domain.events.DomainEvent; // Ou evento concreto ex: PersonagemCriadoEvent
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.command.personagem.CriarPersonagemCommand;
import com.draftstd.hydraforge.ports.in.personagem.CriarPersonagemUseCase;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.personagem.SalvarPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CriarPersonagemUseCaseImpl implements CriarPersonagemUseCase {

    private final SalvarPersonagemPort salvarPersonagemPort;
    private final EventStorePort eventStorePort;

    public CriarPersonagemUseCaseImpl(SalvarPersonagemPort salvarPersonagemPort, EventStorePort eventStorePort) {
        this.salvarPersonagemPort = salvarPersonagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public PersonagemQueryResponse executar(CriarPersonagemCommand command) {
        Personagem personagem = new Personagem();
        personagem.setId(UUID.randomUUID());
        personagem.setUsuarioId(command.usuarioId());
        personagem.setCampanhaId(command.campanhaId());
        personagem.setNome(command.nome());
        personagem.setAtributos(command.atributos());

        Personagem salvo = salvarPersonagemPort.salvar(personagem);

        DomainEvent eventoCriado = new DomainEvent() {}; // Substituir pela classe concreta do evento se disponível
        eventStorePort.saveEvents(salvo.getId().toString(), List.of(eventoCriado), 0L);

        return toQueryResponse(salvo);
    }

    private PersonagemQueryResponse toQueryResponse(Personagem p) {
        return new PersonagemQueryResponse(
                p.getId(),
                p.getUsuarioId(),
                p.getCampanhaId(),
                p.getNome(),
                p.getAtributos()
        );
    }
}