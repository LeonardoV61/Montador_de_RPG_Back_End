package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.domain.events.sessao.StatusSessaoAlteradoEvent;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.command.sessao.AlterarStatusSessaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarStatusSessaoUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.sessao.ObterSessaoPort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarSessaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class GerenciarStatusSessaoUseCaseImpl implements GerenciarStatusSessaoUseCase {

    private final ObterSessaoPort obterSessaoPort;
    private final SalvarSessaoPort salvarSessaoPort;
    private final EventStorePort eventStorePort;

    public GerenciarStatusSessaoUseCaseImpl(ObterSessaoPort obterSessaoPort,
                                            SalvarSessaoPort salvarSessaoPort,
                                            EventStorePort eventStorePort) {
        this.obterSessaoPort = obterSessaoPort;
        this.salvarSessaoPort = salvarSessaoPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    @Transactional
    public SessaoQueryResponse alterarStatus(AlterarStatusSessaoCommand command) {
        Sessao sessao = obterSessaoPort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        var statusAnterior = sessao.getStatus();
        sessao.alterarStatus(command.novoStatus());

        Sessao sessaoSalva = salvarSessaoPort.salvar(sessao);

        StatusSessaoAlteradoEvent event = new StatusSessaoAlteradoEvent(
                sessao.getId(),
                statusAnterior,
                command.novoStatus(),
                Instant.now()
        );

        eventStorePort.saveEvents(
                sessao.getId().toString(),
                List.of(event),
                sessao.getVersao() != null ? sessao.getVersao() : 1L
        );

        return new SessaoQueryResponse(
                sessaoSalva.getId(),
                sessaoSalva.getCampanhaId(),
                sessaoSalva.getTitulo(),
                sessaoSalva.getDescricao(),
                sessaoSalva.getStatus(),
                sessaoSalva.getAgendadaPara()
        );
    }
}