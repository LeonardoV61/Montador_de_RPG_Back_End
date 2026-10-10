package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;
import com.draftstd.hydraforge.ports.in.sistema.CriarSistemaUseCase;
import com.draftstd.hydraforge.ports.out.sistema.SalvarSistemaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CriarSistemaUseCaseImpl implements CriarSistemaUseCase {

    private final SalvarSistemaPort salvarSistemaPort;

    public CriarSistemaUseCaseImpl(SalvarSistemaPort salvarSistemaPort) {
        this.salvarSistemaPort = salvarSistemaPort;
    }

    @Override
    public SistemaQueryResponse executar(CriarSistemaCommand command) {
        Sistema sistema = new Sistema();
        sistema.setId(UUID.randomUUID());
        sistema.setNome(command.nome());
        sistema.setDescricao(command.descricao());
        sistema.setVersao(command.versao());

        Sistema salvo = salvarSistemaPort.salvar(sistema);

        return new SistemaQueryResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getDescricao(),
                salvo.getVersao()
        );
    }
}