package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.application.request.sistema.DeletarSistemaCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.ports.in.sistema.DeletarSistemaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ObterSistemaPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.SalvarSistemaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeletarSistemaUseCaseImpl implements DeletarSistemaUseCase {

    private final ObterSistemaPort obterSistemaPort;
    private final SalvarSistemaPort salvarSistemaPort;

    public DeletarSistemaUseCaseImpl(ObterSistemaPort obterSistemaPort, SalvarSistemaPort salvarSistemaPort) {
        this.obterSistemaPort = obterSistemaPort;
        this.salvarSistemaPort = salvarSistemaPort;
    }

    @Override
    public void executar(DeletarSistemaCommand command) {
        if (!obterSistemaPort.buscarPorId(command.sistemaId()).isPresent()) {
            throw new RecursoNaoEncontradoException("Sistema não encontrado para deleção com ID: " + command.sistemaId());
        }
        salvarSistemaPort.deletarPorId(command.sistemaId());
    }
}