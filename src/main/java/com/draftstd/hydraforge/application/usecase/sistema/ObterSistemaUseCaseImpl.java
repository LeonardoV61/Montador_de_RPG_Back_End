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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ObterSistemaUseCaseImpl implements ObterSistemaUseCase {

    private final ObterSistemaPort obterSistemaPort;

    public ObterSistemaUseCaseImpl(ObterSistemaPort obterSistemaPort) {
        this.obterSistemaPort = obterSistemaPort;
    }

    private SistemaQueryResponse mapearParaResponse(Sistema s) {
        UUID sistemaUuid = s.getId() != null 
                ? UUID.nameUUIDFromBytes(s.getId().toString().getBytes()) 
                : UUID.randomUUID();

        UUID autorId = (s.getUsuario() != null && s.getUsuario().getId() != null)
                ? UUID.nameUUIDFromBytes(s.getUsuario().getId().toString().getBytes())
                : null;

        String versaoStr = s.getVersaoSchemas() != null 
                ? String.valueOf(s.getVersaoSchemas()) 
                : "1";

        return new SistemaQueryResponse(
                sistemaUuid, 
                s.getNome(), 
                s.getDescricao(), 
                versaoStr,
                autorId,
                null,
                List.of(),
                s.getCriadoEm(),
                LocalDateTime.now()
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