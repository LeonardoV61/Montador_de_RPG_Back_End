package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.ObterCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.campanha.ListarCampanhasPorUsuarioQuery;
import com.draftstd.hydraforge.ports.in.query.campanha.ObterCampanhaQuery;
import com.draftstd.hydraforge.ports.out.campanha.ObterCampanhaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ObterCampanhaUseCaseImpl implements ObterCampanhaUseCase {

    private final ObterCampanhaPort obterCampanhaPort;

    public ObterCampanhaUseCaseImpl(ObterCampanhaPort obterCampanhaPort) {
        this.obterCampanhaPort = obterCampanhaPort;
    }

    @Override
    public CampanhaQueryResponse obterPorId(ObterCampanhaQuery query) {
        Campanha campanha = obterCampanhaPort.buscarPorId(query.campanhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada: " + query.campanhaId()));

        return new CampanhaQueryResponse(
                campanha.getId(),
                campanha.getTitulo(),
                campanha.getDescricao(),
                campanha.getSistemaId(),
                campanha.getMestreId(),
                campanha.getStatus(),
                campanha.getCriadaEm()
        );
    }

    @Override
    public List<CampanhaQueryResponse> listarPorUsuario(ListarCampanhasPorUsuarioQuery query) {
        return obterCampanhaPort.listarPorUsuarioId(query.usuarioId())
                .stream()
                .map(c -> new CampanhaQueryResponse(
                        c.getId(),
                        c.getTitulo(),
                        c.getDescricao(),
                        c.getSistemaId(),
                        c.getMestreId(),
                        c.getStatus(),
                        c.getCriadaEm()
                ))
                .toList();
    }
}