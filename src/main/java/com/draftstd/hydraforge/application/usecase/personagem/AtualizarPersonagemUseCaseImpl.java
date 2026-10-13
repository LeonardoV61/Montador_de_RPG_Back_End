package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.domain.events.personagem.*;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.command.personagem.*;
import com.draftstd.hydraforge.ports.in.personagem.*;
import com.draftstd.hydraforge.ports.in.query.personagem.*;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.personagem.ObterPersonagemPort;
import com.draftstd.hydraforge.ports.out.personagem.SalvarPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class AtualizarPersonagemUseCaseImpl implements AtualizarPersonagemUseCase {

    private final ObterPersonagemPort obterPersonagemPort;
    private final SalvarPersonagemPort salvarPersonagemPort;
    private final EventStorePort eventStorePort;

    public AtualizarPersonagemUseCaseImpl(ObterPersonagemPort obterPersonagemPort, SalvarPersonagemPort salvarPersonagemPort, EventStorePort eventStorePort) {
        this.obterPersonagemPort = obterPersonagemPort;
        this.salvarPersonagemPort = salvarPersonagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public PersonagemQueryResponse executar(AtualizarPersonagemCommand command) {
        Personagem p = obterPersonagemPort.buscarPorId(command.personagemId()).orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + command.personagemId()));

        p.setHistoria(command.conceito());

        Personagem atualizado = salvarPersonagemPort.salvar(p);
        long versao = 1L;

        UUID personagemUuid = atualizado.getId() != null ? UUID.nameUUIDFromBytes(atualizado.getId().toString().getBytes()) : UUID.randomUUID();

        UUID usuarioId = null; 
        UUID sistemaId = null;
        UUID campanhaId = (atualizado.getCampanha() != null && atualizado.getCampanha().getId() != null) ? UUID.nameUUIDFromBytes(atualizado.getCampanha().getId().toString().getBytes()) : null;

        Map<String, Object> atributosMap = Map.of(); 

        PersonagemAtualizadoEvent evento = new PersonagemAtualizadoEvent(UUID.randomUUID(), atualizado.getId().toString(), command.nome(), atributosMap, versao,  Instant.now());
        eventStorePort.saveEvents(atualizado.getId().toString(), List.of(evento), versao - 1);

        return new PersonagemQueryResponse(personagemUuid, usuarioId, sistemaId, campanhaId, command.nome(), command.conceito(), command.dadosJson() != null ? command.dadosJson().toString() : null, atualizado.getCriadoEm(), LocalDateTime.now());
    }
}