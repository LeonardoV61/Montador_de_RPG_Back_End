package com.draftstd.hydraforge.application.usecase.sessao.anotacao;

import com.draftstd.hydraforge.application.request.sessao.AtualizarAnotacaoCommand;
import com.draftstd.hydraforge.application.request.sessao.CriarAnotacaoCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarAnotacaoUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.AnotacaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class GerenciarAnotacaoUseCaseImpl implements GerenciarAnotacaoUseCase {

    private final AnotacaoPersistencePort anotacaoPersistencePort;

    public GerenciarAnotacaoUseCaseImpl(AnotacaoPersistencePort anotacaoPersistencePort) {
        this.anotacaoPersistencePort = anotacaoPersistencePort;
    }

    @Override
    public UUID criarAnotacao(CriarAnotacaoCommand command) {
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

    @Override
    public void atualizarAnotacao(AtualizarAnotacaoCommand command) {
        Anotacao anotacao = anotacaoPersistencePort.buscarPorId(command.anotacaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anotação não encontrada: " + command.anotacaoId()));

        anotacao.atualizar(
                command.titulo(),
                command.conteudo(),
                command.categoria(),
                command.ePrivado()
        );

        anotacaoPersistencePort.salvar(anotacao);
    }
}