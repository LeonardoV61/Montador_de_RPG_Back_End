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
        Sistema sistema = obterSistemaPort.buscarPorId(command.sistemaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema não encontrado: " + command.sistemaId()));

        sistema.setNome(command.nome());
        sistema.setDescricao(command.descricao());
        sistema.setVersao(command.versao());

        Sistema atualizado = salvarSistemaPort.salvar(sistema);

        return new SistemaQueryResponse(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getDescricao(),
                atualizado.getVersao()
        );
    }
}