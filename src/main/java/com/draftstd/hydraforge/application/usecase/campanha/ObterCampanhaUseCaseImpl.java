package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.application.response.campanha.CampanhaParticipanteQueryResponse;
import com.draftstd.hydraforge.application.response.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.application.response.campanha.ListarCampanhasPorUsuarioQuery;
import com.draftstd.hydraforge.application.response.campanha.ObterCampanhaQuery;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.ObterCampanhaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.campanha.CampanhaPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ObterCampanhaUseCaseImpl implements ObterCampanhaUseCase {

    private final CampanhaPersistencePort campanhaPersistencePort;

    public ObterCampanhaUseCaseImpl(CampanhaPersistencePort campanhaPersistencePort) {
        this.campanhaPersistencePort = campanhaPersistencePort;
    }

    @Override
    @Transactional(readOnly = true)
    public CampanhaQueryResponse obterPorId(ObterCampanhaQuery query) {
        Campanha campanha = campanhaPersistencePort.buscarPorId(query.campanhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada com o ID: " + query.campanhaId()));

        return mapToResponse(campanha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampanhaQueryResponse> listarPorUsuario(ListarCampanhasPorUsuarioQuery query) {
        return campanhaPersistencePort.listarPorUsuario(query.usuarioId()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CampanhaQueryResponse mapToResponse(Campanha c) {
        List<CampanhaParticipanteQueryResponse> participantesResponse = c.getMembros().stream()
                .map(p -> new CampanhaParticipanteQueryResponse(
                        p.usuarioId(),
                        null, // Nome resolvido via projeção na visualização se necessário
                        p.papel(),
                        p.status(),
                        p.entrouEm()
                ))
                .toList();

        return new CampanhaQueryResponse(
                c.getId(),
                c.getNome(),
                c.getDescricao(),
                c.getUrlImagem(),
                c.getStatus(),
                c.getSistemaId(),
                c.getMestreId(),
                participantesResponse,
                c.getSessoesIds(),
                c.getPersonagensIds(),
                c.getInstanciasIds(),
                c.getCriadaEm(),
                c.getAtualizadaEm()
        );
    }
}