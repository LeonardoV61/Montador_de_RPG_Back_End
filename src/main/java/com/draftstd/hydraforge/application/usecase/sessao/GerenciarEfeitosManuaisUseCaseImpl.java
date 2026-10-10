package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.EfeitoAtivo;
import com.draftstd.hydraforge.ports.in.command.sessao.AdicionarEfeitoManualCommand;
import com.draftstd.hydraforge.ports.in.command.sessao.RemoverEfeitoManualCommand;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarEfeitosManuaisUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterSessaoPort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarSessaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class GerenciarEfeitosManuaisUseCaseImpl implements GerenciarEfeitosManuaisUseCase {

    private final ObterSessaoPort obterSessaoPort;
    private final SalvarSessaoPort salvarSessaoPort;

    public GerenciarEfeitosManuaisUseCaseImpl(ObterSessaoPort obterSessaoPort, SalvarSessaoPort salvarSessaoPort) {
        this.obterSessaoPort = obterSessaoPort;
        this.salvarSessaoPort = salvarSessaoPort;
    }

    @Override
    public void adicionarEfeito(AdicionarEfeitoManualCommand command) {
        obterSessaoPort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        EfeitoAtivo efeito = new EfeitoAtivo();
        efeito.setId(UUID.randomUUID());
        efeito.setSessaoId(command.sessaoId());
        efeito.setAlvoId(command.alvoId());
        efeito.setNome(command.nomeEfeito());
        efeito.setDuracaoRodadas(command.duracaoRodadas());
        efeito.setDescricao(command.descricao());

        // Lógica de persistência / adição de efeito na sessão
    }

    @Override
    public void removerEfeito(RemoverEfeitoManualCommand command) {
        obterSessaoPort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        // Lógica de remoção do efeito por ID
    }
}