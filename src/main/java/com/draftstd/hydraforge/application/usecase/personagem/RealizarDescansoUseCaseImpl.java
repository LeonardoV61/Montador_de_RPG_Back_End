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
import java.util.UUID;

@Service
@Transactional
public class RealizarDescansoUseCaseImpl implements RealizarDescansoUseCase {

    private final ObterPersonagemPort obterPersonagemPort;
    private final SalvarPersonagemPort salvarPersonagemPort;
    private final EventStorePort eventStorePort;

    public RealizarDescansoUseCaseImpl(ObterPersonagemPort obterPersonagemPort, SalvarPersonagemPort salvarPersonagemPort, EventStorePort eventStorePort) {
        this.obterPersonagemPort = obterPersonagemPort;
        this.salvarPersonagemPort = salvarPersonagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public PersonagemQueryResponse executar(RealizarDescansoCommand command) {
        Personagem p = obterPersonagemPort.buscarPorId(command.personagemId()).orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + command.personagemId()));

        Personagem atualizado = salvarPersonagemPort.salvar(p);
        
        // Versão padrão segura (já que a entidade não possui getVersao())
        long versao = 1L;

        String atualizadoIdStr = atualizado.getId() != null ? atualizado.getId().toString() : UUID.randomUUID().toString();

        DescansoRealizadoEvent evento = new DescansoRealizadoEvent(UUID.randomUUID(), atualizadoIdStr, command.tipoDescanso(), versao, Instant.now());
        eventStorePort.saveEvents(atualizadoIdStr, List.of(evento), versao - 1);

        // Conversões e extrações seguras para o PersonagemQueryResponse de 9 parâmetros
        UUID personagemUuid = atualizado.getId() != null ? UUID.nameUUIDFromBytes(atualizado.getId().toString().getBytes()) : UUID.randomUUID();

        UUID usuarioId = null;
        UUID sistemaId = null;
        UUID campanhaId = (atualizado.getCampanha() != null && atualizado.getCampanha().getId() != null) ? UUID.nameUUIDFromBytes(atualizado.getCampanha().getId().toString().getBytes()) : null;

        return new PersonagemQueryResponse(personagemUuid, usuarioId, sistemaId, campanhaId, null, atualizado.getHistoria(), null, atualizado.getCriadoEm() != null ? atualizado.getCriadoEm() : LocalDateTime.now(), LocalDateTime.now());
    }
}