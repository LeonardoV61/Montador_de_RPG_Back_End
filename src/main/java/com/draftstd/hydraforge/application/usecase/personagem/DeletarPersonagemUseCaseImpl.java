package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.domain.events.personagem.*;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.command.personagem.*;
import com.draftstd.hydraforge.ports.in.personagem.*;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.personagem.DeletarPersonagemPort;
import com.draftstd.hydraforge.ports.out.personagem.ObterPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DeletarPersonagemUseCaseImpl implements DeletarPersonagemUseCase {

    private final ObterPersonagemPort obterPersonagemPort;
    private final DeletarPersonagemPort deletarPersonagemPort;
    private final EventStorePort eventStorePort;

    public DeletarPersonagemUseCaseImpl(ObterPersonagemPort obterPersonagemPort, DeletarPersonagemPort deletarPersonagemPort, EventStorePort eventStorePort) {
        this.obterPersonagemPort = obterPersonagemPort;
        this.deletarPersonagemPort = deletarPersonagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public void executar(DeletarPersonagemCommand command) {
        Personagem p = obterPersonagemPort.buscarPorId(command.personagemId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + command.personagemId()));

        // Definimos uma versão padrão segura (ou 1L) já que a entidade não possui getVersao()
        long versao = 1L;
        
        PersonagemDeletadoEvent evento = new PersonagemDeletadoEvent(UUID.randomUUID(), p.getId().toString(), versao, Instant.now());
        eventStorePort.saveEvents(p.getId().toString(), List.of(evento), versao - 1);

        deletarPersonagemPort.deletarPorId(command.personagemId());
    }
}