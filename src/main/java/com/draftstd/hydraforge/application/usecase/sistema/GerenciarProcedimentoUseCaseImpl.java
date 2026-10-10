package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.domain.events.DomainEvent; // Ou evento concreto ex: ProcedimentoExecutadoEvent
import com.draftstd.hydraforge.ports.in.command.sistema.ExecutarProcedimentoCommand;
import com.draftstd.hydraforge.ports.in.sistema.GerenciarProcedimentoUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ProcedimentoOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GerenciarProcedimentoUseCaseImpl implements GerenciarProcedimentoUseCase {

    private final ProcedimentoOutputPort procedimentoOutputPort;
    private final EventStorePort eventStorePort;

    public GerenciarProcedimentoUseCaseImpl(ProcedimentoOutputPort procedimentoOutputPort,
                                             EventStorePort eventStorePort) {
        this.procedimentoOutputPort = procedimentoOutputPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public Object executarProcedimento(ExecutarProcedimentoCommand command) {
        Object resultado = procedimentoOutputPort.executar(command.procedimentoId(), command.contexto());

        DomainEvent eventoExecucao = new DomainEvent() {};
        if (command.procedimentoId() != null) {
            eventStorePort.saveEvents(command.procedimentoId().toString(), List.of(eventoExecucao), 1L);
        }

        return resultado;
    }
}