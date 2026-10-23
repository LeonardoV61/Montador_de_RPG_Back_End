package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.application.request.sistema.ExecutarProcedimentoCommand;
import com.draftstd.hydraforge.domain.events.mecanica.ProcedimentoExecutadoEvent;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.ports.in.sistema.GerenciarProcedimentoUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ProcedimentoOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GerenciarProcedimentoUseCaseImpl implements GerenciarProcedimentoUseCase {

    private final ProcedimentoOutputPort procedimentoOutputPort;
    private final EventStorePort eventStorePort;

    public GerenciarProcedimentoUseCaseImpl(ProcedimentoOutputPort procedimentoOutputPort, EventStorePort eventStorePort) {
        this.procedimentoOutputPort = procedimentoOutputPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public ResultadoEtapa executarProcedimento(ExecutarProcedimentoCommand command) {
        Procedimento procedimento = procedimentoOutputPort.buscarPorId(command.procedimentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Procedimento não encontrado: " + command.procedimentoId()));

        String aggregateId = procedimento.getId().toString();
        long versao = 1L;

        ProcedimentoExecutadoEvent evento = new ProcedimentoExecutadoEvent(
                UUID.randomUUID(),
                aggregateId,
                procedimento.getId(),
                versao,
                Instant.now()
        );

        eventStorePort.salvarEventos(aggregateId, List.of(evento), versao - 1);

        // Retorno ajustado utilizando o método estático real concluida(...)
        return ResultadoEtapa.concluida("Procedimento " + procedimento.getNome() + " executado com sucesso.");
    }
}