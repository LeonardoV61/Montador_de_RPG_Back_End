package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.ports.in.query.sistema.ListarSistemasQuery;
import com.draftstd.hydraforge.ports.in.query.sistema.ObterSistemaQuery;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;
import com.draftstd.hydraforge.ports.in.sistema.ObterSistemaUseCase;
import com.draftstd.hydraforge.ports.out.sistema.ObterSistemaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ObterSistemaUseCaseImpl implements ObterSistemaUseCase {

    private final ObterSistemaPort obterSistemaPort;

    public ObterSistemaUseCaseImpl(ObterSistemaPort obterSistemaPort) {
        this.obterSistemaPort = obterSistemaPort;
    }

    @Override
    public SistemaQueryResponse obterPorId(ObterSistemaQuery query) {
        Sistema sistema = obterSistemaPort.buscarPorId(query.sistemaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema não encontrado: " + query.sistemaId()));

        return new SistemaQueryResponse(
                sistema.getId(),
                sistema.getNome(),
                sistema.getDescricao(),
                sistema.getVersao()
        );
    }

    @Override
    public List<SistemaQueryResponse> listarTodos(ListarSistemasQuery query) {
        return obterSistemaPort.listarTodos()
                .stream()
                .map(s -> new SistemaQueryResponse(
                        s.getId(),
                        s.getNome(),
                        s.getDescricao(),
                        s.getVersao()
                ))
                .toList();
    }
}