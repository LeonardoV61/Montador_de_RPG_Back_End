package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.application.request.sessao.AdicionarEfeitoManualCommand;
import com.draftstd.hydraforge.application.request.sessao.RemoverEfeitoManualCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.EfeitoAtivo;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarEfeitosManuaisUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.EfeitoAtivoPersistencePort;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class GerenciarEfeitosManuaisUseCaseImpl implements GerenciarEfeitosManuaisUseCase {

    private final SessaoPersistencePort sessaoPersistencePort;
    private final EfeitoAtivoPersistencePort efeitoAtivoPersistencePort;

    public GerenciarEfeitosManuaisUseCaseImpl(SessaoPersistencePort sessaoPersistencePort, EfeitoAtivoPersistencePort efeitoAtivoPersistencePort) {
        this.sessaoPersistencePort = sessaoPersistencePort;
        this.efeitoAtivoPersistencePort = efeitoAtivoPersistencePort;
    }

    @Override
    public void adicionarEfeito(AdicionarEfeitoManualCommand command) {
        Sessao sessao = sessaoPersistencePort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        EfeitoAtivo efeito = new EfeitoAtivo(
                UUID.randomUUID(),
                command.entidadeInstanciaId(),
                command.entidadeProcedimentoId(),
                command.parametrosJson(),
                command.momento(),
                command.expiraEm(),
                true,
                command.usosRestantes()
        );

        sessao.adicionarEfeitoAtivo(efeito);
        efeitoAtivoPersistencePort.salvar(sessao.getId(), efeito);
    }

    @Override
    public void removerEfeito(RemoverEfeitoManualCommand command) {
        sessaoPersistencePort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        efeitoAtivoPersistencePort.remover(command.efeitoId());
    }
}