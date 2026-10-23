package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.application.request.personagem.CriarPersonagemCommand;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.personagem.CriarPersonagemUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.personagem.SalvarPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UUID executar(CriarPersonagemCommand command) {
        // Instancia o Agregado executando as validações e gerando o PersonagemCriadoEvent
        Personagem personagem = new Personagem(
            UUID.randomUUID(),
            command.nome(),
            command.usuarioId(),
            command.campanhaId(),
            command.instanciaId(),
            command.historia(),
            command.aparencia(),
            command.urlImagem(),
            command.notasJogador(),
            command.atributos()
        );

        // Persiste os eventos gerados pelo Agregado no Event Store
        eventStorePort.salvarEventos(personagem.getId().toString(), personagem.getUncommittedEvents(), 0L);
        personagem.markEventsAsCommitted();

        // Atualiza a projeção de leitura
        salvarPersonagemPort.salvar(personagem);

        return personagem.getId();
    }
}