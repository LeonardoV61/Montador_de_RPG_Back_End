package com.draftstd.hydraforge.application.usecase.mecanica;

import com.draftstd.hydraforge.domain.events.mecanica.TesteLivreRoladoEvent;
import com.draftstd.hydraforge.ports.in.command.mecanica.RolarDadosCommand;
import com.draftstd.hydraforge.ports.in.mecanica.RolarDadosUseCase;
import com.draftstd.hydraforge.ports.in.query.mecanica.ResultadoRolagemQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.mecanica.SalvarHistoricoRolagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RolarTesteLivreUseCaseImpl implements RolarDadosUseCase {

    private final SalvarHistoricoRolagemPort salvarHistoricoRolagemPort;
    private final EventStorePort eventStorePort;

    public RolarTesteLivreUseCaseImpl(SalvarHistoricoRolagemPort salvarHistoricoRolagemPort, EventStorePort eventStorePort) {
        this.salvarHistoricoRolagemPort = salvarHistoricoRolagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public ResultadoRolagemQueryResponse executar(RolarDadosCommand command) {
        UUID rolagemId = UUID.randomUUID();
        
        ResultadoRolagemQueryResponse resultado = new ResultadoRolagemQueryResponse(rolagemId, command.sessaoId(), command.jogadorId(), command.personagemId(), command.expressaoDados(), List.of(15, 3), 3, 18, command.motivo(), LocalDateTime.now());

        salvarHistoricoRolagemPort.salvar(resultado);

        TesteLivreRoladoEvent evento = new TesteLivreRoladoEvent(UUID.randomUUID(), rolagemId.toString(), command.personagemId(), command.expressaoDados(), 18, 1L, Instant.now());
        eventStorePort.saveEvents(rolagemId.toString(), List.of(evento), 0L);

        return resultado;
    }
}