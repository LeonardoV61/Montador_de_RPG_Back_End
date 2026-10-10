package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.ports.in.command.sessao.EntrarSessaoCommand;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarParticipantesSessaoUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterSessaoPort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarSessaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GerenciarParticipantesSessaoUseCaseImpl implements GerenciarParticipantesSessaoUseCase {

    private final ObterSessaoPort obterSessaoPort;
    private final SalvarSessaoPort salvarSessaoPort;

    public GerenciarParticipantesSessaoUseCaseImpl(ObterSessaoPort obterSessaoPort, SalvarSessaoPort salvarSessaoPort) {
        this.obterSessaoPort = obterSessaoPort;
        this.salvarSessaoPort = salvarSessaoPort;
    }

    @Override
    public void entrarSessao(EntrarSessaoCommand command) {
        obterSessaoPort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        // Lógica de vinculação do jogador/personagem à sessão ativa
    }
}