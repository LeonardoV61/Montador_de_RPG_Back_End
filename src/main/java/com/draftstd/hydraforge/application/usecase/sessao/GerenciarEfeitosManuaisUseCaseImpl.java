package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.EfeitoAtivo;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.command.sessao.AdicionarEfeitoManualCommand;
import com.draftstd.hydraforge.ports.in.command.sessao.RemoverEfeitoManualCommand;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarEfeitosManuaisUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterSessaoPort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarSessaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Sessao sessao = obterSessaoPort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        EfeitoAtivo efeito = new EfeitoAtivo();
        // O id é gerado por sequence (Long), não chamamos setId com UUID
        
        efeito.setSessao(sessao);
        efeito.setMomento("MANUAL");
        efeito.setExpiraEm(1);
        efeito.setAtivo(true);

        // Se precisar associar parâmetros ou dados adicionais do comando, mapeie aqui.
        
        // Lógica de salvamento ou adição do efeito na sessão correspondente
    }

    @Override
    public void removerEfeito(RemoverEfeitoManualCommand command) {
        obterSessaoPort.buscarPorId(command.sessaoId()).orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        // Lógica de remoção do efeito por ID
    }
}