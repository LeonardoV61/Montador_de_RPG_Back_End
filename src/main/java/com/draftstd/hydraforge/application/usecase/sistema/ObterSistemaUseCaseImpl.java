package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.application.response.sistema.ListarSistemasQuery;
import com.draftstd.hydraforge.application.response.sistema.ObterSistemaQuery;
import com.draftstd.hydraforge.application.response.sistema.SistemaQueryResponse;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.ports.in.sistema.ObterSistemaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ObterSistemaPort;
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

    private SistemaQueryResponse mapearParaResponse(Sistema s) {
        return new SistemaQueryResponse(
                s.getId(),
                s.getUsuarioCriadorId(),
                s.getSistemaPaiId(),
                s.getNome(),
                s.getDescricao(),
                s.getUrlImagem(),
                s.getConfiguracao(),
                s.getSchemaAtributos(),
                s.getSchemaEntidades(),
                s.getSchemaResolucoes(),
                s.getVersaoSchemas(),
                s.isEOficial(),
                s.getCriadoEm(),
                List.of()
        );
    }

    @Override
    public SistemaQueryResponse obterPorId(ObterSistemaQuery query) {
        Sistema s = obterSistemaPort.buscarPorId(query.sistemaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema não encontrado: " + query.sistemaId()));
        return mapearParaResponse(s);
    }

    @Override
    public List<SistemaQueryResponse> listarSistemas(ListarSistemasQuery query) {
        return obterSistemaPort.listarTodos().stream()
                .map(this::mapearParaResponse)
                .toList();
    }
}