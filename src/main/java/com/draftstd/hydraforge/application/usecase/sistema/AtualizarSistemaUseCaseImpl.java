package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;
import com.draftstd.hydraforge.ports.in.sistema.AtualizarSistemaUseCase;
import com.draftstd.hydraforge.ports.out.sistema.ObterSistemaPort;
import com.draftstd.hydraforge.ports.out.sistema.SalvarSistemaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AtualizarSistemaUseCaseImpl implements AtualizarSistemaUseCase {

    private final ObterSistemaPort obterSistemaPort;
    private final SalvarSistemaPort salvarSistemaPort;

    public AtualizarSistemaUseCaseImpl(ObterSistemaPort obterSistemaPort, SalvarSistemaPort salvarSistemaPort) {
        this.obterSistemaPort = obterSistemaPort;
        this.salvarSistemaPort = salvarSistemaPort;
    }

    @Override
    public SistemaQueryResponse executar(AtualizarSistemaCommand command) {
        Sistema s = obterSistemaPort.buscarPorId(command.sistemaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema não encontrado: " + command.sistemaId()));

        s.setNome(command.nome());
        s.setDescricao(command.descricao());
        
        if (command.versao() != null) {
            s.setVersaoSchemas(Integer.valueOf(command.versao()));
        }

        Sistema atualizado = salvarSistemaPort.salvar(s);

        UUID sistemaUuid = atualizado.getId() != null 
                ? UUID.nameUUIDFromBytes(atualizado.getId().toString().getBytes()) 
                : UUID.randomUUID();

        UUID autorId = (atualizado.getUsuario() != null && atualizado.getUsuario().getId() != null)
                ? UUID.nameUUIDFromBytes(atualizado.getUsuario().getId().toString().getBytes())
                : null;

        String versaoStr = atualizado.getVersaoSchemas() != null 
                ? String.valueOf(atualizado.getVersaoSchemas()) 
                : "1";

        return new SistemaQueryResponse(
                sistemaUuid, 
                atualizado.getNome(), 
                atualizado.getDescricao(), 
                versaoStr,
                autorId,
                null,
                List.of(),
                atualizado.getCriadoEm(),
                LocalDateTime.now()
        );
    }
}