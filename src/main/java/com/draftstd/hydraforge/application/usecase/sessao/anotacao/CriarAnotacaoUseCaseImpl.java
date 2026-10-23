package com.draftstd.hydraforge.application.usecase.sessao.anotacao;

import com.draftstd.hydraforge.application.request.sessao.CriarAnotacaoCommand;
import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.ports.in.sessao.CriarAnotacaoUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.AnotacaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CriarAnotacaoUseCaseImpl implements CriarAnotacaoUseCase {

    private final AnotacaoPersistencePort anotacaoPersistencePort;

    public CriarAnotacaoUseCaseImpl(AnotacaoPersistencePort anotacaoPersistencePort) {
        this.anotacaoPersistencePort = anotacaoPersistencePort;
    }

    @Override
    @Transactional
    public UUID executar(CriarAnotacaoCommand command) {
        UUID novoId = UUID.randomUUID();

        Anotacao anotacao = new Anotacao(
                novoId,
                command.usuarioId(),
                command.campanhaId(),
                command.sessaoId(),
                command.titulo(),
                command.conteudo(),
                command.categoria(),
                command.anotacaoComplexaJson(),
                command.ePrivado()
        );

        anotacaoPersistencePort.salvar(anotacao);
        return novoId;
    }
}