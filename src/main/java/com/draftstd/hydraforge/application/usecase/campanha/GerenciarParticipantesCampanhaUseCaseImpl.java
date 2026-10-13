package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.domain.events.campanha.ParticipanteCampanhaAlteradoEvent;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.GerenciarParticipantesCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.command.campanha.AdicionarJogadorCampanhaCommand;
import com.draftstd.hydraforge.ports.in.command.campanha.AlterarPapelJogadorCommand;
import com.draftstd.hydraforge.ports.in.command.campanha.RemoverJogadorCampanhaCommand;
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
public class GerenciarParticipantesCampanhaUseCaseImpl implements GerenciarParticipantesCampanhaUseCase {

    private final ObterCampanhaPort obterCampanhaPort;
    private final SalvarCampanhaPort salvarCampanhaPort;
    private final EventStorePort eventStorePort;

    public GerenciarParticipantesCampanhaUseCaseImpl(ObterCampanhaPort obterCampanhaPort, SalvarCampanhaPort salvarCampanhaPort, EventStorePort eventStorePort) {
        this.obterCampanhaPort = obterCampanhaPort;
        this.salvarCampanhaPort = salvarCampanhaPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public CampanhaQueryResponse adicionarJogador(AdicionarJogadorCampanhaCommand command) {
        return processarAlteracao(command.campanhaId(), command.usuarioId(), "ADICIONAR");
    }

    @Override
    public CampanhaQueryResponse removerJogador(RemoverJogadorCampanhaCommand command) {
        return processarAlteracao(command.campanhaId(), command.usuarioId(), "REMOVER");
    }

    @Override
    public CampanhaQueryResponse alterarPapel(AlterarPapelJogadorCommand command) {
        return processarAlteracao(command.campanhaId(), command.usuarioId(), "ALTERAR_PAPEL");
    }
    
    private CampanhaQueryResponse processarAlteracao(UUID campanhaId, UUID usuarioId, String acao) {
        Campanha campanha = obterCampanhaPort.buscarPorId(campanhaId).orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada: " + campanhaId));
        Campanha salva = salvarCampanhaPort.salvar(campanha);
        long versao = 1L;
        String campanhaIdStr = salva.getId() != null ? salva.getId().toString() : campanhaId.toString();
        ParticipanteCampanhaAlteradoEvent evento = new ParticipanteCampanhaAlteradoEvent(UUID.randomUUID(), campanhaIdStr, usuarioId, acao, versao, Instant.now());
        eventStorePort.saveEvents(campanhaIdStr, List.of(evento), versao - 1);
        return toQueryResponse(salva);
    }

    private CampanhaQueryResponse toQueryResponse(Campanha c) {
        List<CampanhaParticipanteQueryResponse> participantesResponse = (c.getUsuarios() != null) ? c.getUsuarios().stream().map(p -> new CampanhaParticipanteQueryResponse(
            p.getUsuario() != null ? p.getUsuario().getId() : null,
            p.getUsuario() != null ? p.getUsuario().getNome() : "Desconhecido",
            p.getPapel(),
            p.getEntrouEm()
        )).toList() : List.of();

        return new CampanhaQueryResponse(c.getId() != null ? UUID.nameUUIDFromBytes(c.getId().toString().getBytes()) : null, c.getNome(), c.getDescricao(), c.getSistema() != null && c.getSistema().getId() != null ? UUID.nameUUIDFromBytes(c.getSistema().getId().toString().getBytes()) : null, null, c.getStatus(), participantesResponse, c.getCriadaEm(), LocalDateTime.now());
    }
}