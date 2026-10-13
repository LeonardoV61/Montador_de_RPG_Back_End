package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.domain.events.mecanica.ProcedimentoExecutadoEvent;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.ports.in.command.sistema.ExecutarProcedimentoCommand;
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
    public Object executarProcedimento(ExecutarProcedimentoCommand command) {
        // Convertendo o identificador do command para Long, se necessário, ou buscando pelo ID correto
        UUID procedimentoIdLong = command.procedimentoId() != null 
                ? command.procedimentoId() // Exemplo de conversão se vier UUID, ou ajuste conforme o tipo do command
                : null;

        Procedimento procedimento = procedimentoOutputPort.buscarPorId(procedimentoIdLong)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Procedimento não encontrado: " + command.procedimentoId()));

        // Lógica de execução do procedimento
        Object resultado = procedimento; 

        if (command.procedimentoId() != null) {
            String aggregateId = command.procedimentoId().toString();
            long versao = 1L;

            ProcedimentoExecutadoEvent evento = new ProcedimentoExecutadoEvent(
                    UUID.randomUUID(), 
                    aggregateId, 
                    command.procedimentoId(), 
                    versao, 
                    Instant.now()
            );
            
            eventStorePort.saveEvents(aggregateId, List.of(evento), versao - 1);
        }

        return resultado;
    }
}