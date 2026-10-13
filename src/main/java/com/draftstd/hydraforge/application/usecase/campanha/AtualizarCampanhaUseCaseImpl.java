package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import com.draftstd.hydraforge.domain.events.campanha.CampanhaAtualizadaEvent;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.AtualizarCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.command.campanha.AtualizarCampanhaCommand;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaParticipanteQueryResponse;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.ports.out.campanha.ObterCampanhaPort;
import com.draftstd.hydraforge.ports.out.campanha.SalvarCampanhaPort;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AtualizarCampanhaUseCaseImpl implements AtualizarCampanhaUseCase {

    private final ObterCampanhaPort obterCampanhaPort;
    private final SalvarCampanhaPort salvarCampanhaPort;
    private final EventStorePort eventStorePort;

    public AtualizarCampanhaUseCaseImpl(ObterCampanhaPort obterCampanhaPort, SalvarCampanhaPort salvarCampanhaPort, EventStorePort eventStorePort) {
        this.obterCampanhaPort = obterCampanhaPort;
        this.salvarCampanhaPort = salvarCampanhaPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public CampanhaQueryResponse executar(AtualizarCampanhaCommand command) {
        Campanha campanha = obterCampanhaPort.buscarPorId(command.campanhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada: " + command.campanhaId()));

        // Alterado de titulo() para nome() conforme o command e a entidade
        campanha.setNome(command.nome());
        campanha.setDescricao(command.descricao());
        if (command.status() != null) {
            campanha.setStatus(command.status());
        }

        Campanha salva = salvarCampanhaPort.salvar(campanha);
        
        // Como Campanha não tem campo de versao explícito, usamos 1L por defeito
        long versao = 1L;
        CampanhaAtualizadaEvent evento = new CampanhaAtualizadaEvent(UUID.randomUUID(), salva.getId().toString(), salva.getNome(), salva.getDescricao(), salva.getStatus() != null ? salva.getStatus().name() : null, versao, Instant.now());
        eventStorePort.saveEvents(salva.getId().toString(), List.of(evento), versao);

        List<CampanhaParticipanteQueryResponse> participantesResponse = (salva.getUsuarios() != null) ? salva.getUsuarios().stream().map(participante -> new CampanhaParticipanteQueryResponse(
            participante.getUsuario() != null ? participante.getUsuario().getId() : null,
            participante.getUsuario() != null ? participante.getUsuario().getNome() : "Desconhecido",
            participante.getPapel(),
            participante.getEntrouEm() 
        )).toList() : List.of();

        return new CampanhaQueryResponse(salva.getId() != null ? UUID.nameUUIDFromBytes(salva.getId().toString().getBytes()) : null, salva.getNome(), salva.getDescricao(), salva.getSistema() != null && salva.getSistema().getId() != null ? UUID.nameUUIDFromBytes(salva.getSistema().getId().toString().getBytes()) : null, null, salva.getStatus(), participantesResponse, salva.getCriadaEm(), LocalDateTime.now());
    }
}