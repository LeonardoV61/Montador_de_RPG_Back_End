package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.ObterCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaParticipanteQueryResponse;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.campanha.ListarCampanhasPorUsuarioQuery;
import com.draftstd.hydraforge.ports.in.query.campanha.ObterCampanhaQuery;
import com.draftstd.hydraforge.ports.out.campanha.ObterCampanhaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ObterCampanhaUseCaseImpl implements ObterCampanhaUseCase {

    private final ObterCampanhaPort obterCampanhaPort;

    public ObterCampanhaUseCaseImpl(ObterCampanhaPort obterCampanhaPort) {
        this.obterCampanhaPort = obterCampanhaPort;
    }

    @Override
    public CampanhaQueryResponse obterPorId(ObterCampanhaQuery query) {
        Campanha c = obterCampanhaPort.buscarPorId(query.campanhaId()).orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada: " + query.campanhaId()));
        return toQueryResponse(c);
    }

    @Override
    public List<CampanhaQueryResponse> listarPorUsuario(ListarCampanhasPorUsuarioQuery query) {
        return obterCampanhaPort.listarPorUsuarioId(query.usuarioId()).stream().map(this::toQueryResponse).toList();
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