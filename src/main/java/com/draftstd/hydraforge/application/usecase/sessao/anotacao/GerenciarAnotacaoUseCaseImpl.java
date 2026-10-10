package com.draftstd.hydraforge.application.usecase.sessao.anotacao;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.ports.in.command.sessao.AtualizarAnotacaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarAnotacaoUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterAnotacaoPort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarAnotacaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GerenciarAnotacaoUseCaseImpl implements GerenciarAnotacaoUseCase {

    private final ObterAnotacaoPort obterAnotacaoPort;
    private final SalvarAnotacaoPort salvarAnotacaoPort;

    public GerenciarAnotacaoUseCaseImpl(ObterAnotacaoPort obterAnotacaoPort, SalvarAnotacaoPort salvarAnotacaoPort) {
        this.obterAnotacaoPort = obterAnotacaoPort;
        this.salvarAnotacaoPort = salvarAnotacaoPort;
    }

    @Override
    public AnotacaoQueryResponse atualizar(AtualizarAnotacaoCommand command) {
        Anotacao anotacao = obterAnotacaoPort.buscarPorId(command.anotacaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anotação não encontrada: " + command.anotacaoId()));

        anotacao.setTitulo(command.titulo());
        anotacao.setConteudo(command.conteudo());
        anotacao.setPrivada(command.privada());

        Anotacao atualizada = salvarAnotacaoPort.salvar(anotacao);

        return new AnotacaoQueryResponse(
                atualizada.getId(),
                atualizada.getSessaoId(),
                atualizada.getAutorId(),
                atualizada.getTitulo(),
                atualizada.getConteudo(),
                atualizada.isPrivada(),
                atualizada.getCriadaEm()
        );
    }
}