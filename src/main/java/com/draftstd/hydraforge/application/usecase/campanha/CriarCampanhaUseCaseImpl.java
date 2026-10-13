package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import com.draftstd.hydraforge.domain.events.campanha.CampanhaCriadaEvent;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.ports.in.campanha.CriarCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.command.campanha.CriarCampanhaCommand;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaParticipanteQueryResponse;
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
public class CriarCampanhaUseCaseImpl implements CriarCampanhaUseCase {

    private final SalvarCampanhaPort salvarCampanhaPort;
    private final EventStorePort eventStorePort;

    public CriarCampanhaUseCaseImpl(SalvarCampanhaPort salvarCampanhaPort, EventStorePort eventStorePort) {
        this.salvarCampanhaPort = salvarCampanhaPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public CampanhaQueryResponse executar(CriarCampanhaCommand command) {
        Campanha campanha = new Campanha();  
        campanha.setNome(command.nome()); 
        campanha.setDescricao(command.descricao());
        if (command.sistemaId() != null) {
            Sistema sistema = new Sistema();
            campanha.setSistema(sistema);
        }
        campanha.setStatus(StatusCampanha.ATIVA);
        campanha.setCriadaEm(LocalDateTime.now());
        Campanha salva = salvarCampanhaPort.salvar(campanha);
        String campanhaIdStr = salva.getId() != null ? salva.getId().toString() : UUID.randomUUID().toString();

        CampanhaCriadaEvent evento = new CampanhaCriadaEvent(UUID.randomUUID(),  campanhaIdStr, salva.getNome(), salva.getDescricao(), command.sistemaId(), command.mestreId(), 1L, Instant.now());
        eventStorePort.saveEvents(campanhaIdStr, List.of(evento), 0L);

        return toQueryResponse(salva);
    }

    private CampanhaQueryResponse toQueryResponse(Campanha c) {
        List<CampanhaParticipanteQueryResponse> participantesResponse = (c.getUsuarios() != null) ? c.getUsuarios().stream().map(p -> new CampanhaParticipanteQueryResponse(
            p.getUsuario() != null ? p.getUsuario().getId() : null,
            p.getUsuario() != null ? p.getUsuario().getNome() : "Desconhecido",
            p.getPapel(),
            p.getEntrouEm()
        )).toList(): List.of();

        return new CampanhaQueryResponse(c.getId() != null ? UUID.nameUUIDFromBytes(c.getId().toString().getBytes()) : null, c.getNome(), c.getDescricao(), c.getSistema() != null ? UUID.nameUUIDFromBytes(c.getSistema().getId().toString().getBytes()) : null, null, c.getStatus(), participantesResponse, c.getCriadaEm(), null);
    }
}