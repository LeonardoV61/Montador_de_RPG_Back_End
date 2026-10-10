package com.draftstd.hydraforge.application.usecase.mecanica;

import com.draftstd.hydraforge.domain.events.DomainEvent; // Ou evento concreto ex: TesteLivreRoladoEvent
import com.draftstd.hydraforge.ports.in.command.mecanica.RolarDadosCommand;
import com.draftstd.hydraforge.ports.in.mecanica.RolarTesteLivreUseCase;
import com.draftstd.hydraforge.ports.in.query.mecanica.ResultadoRolagemQueryResponse;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.mecanica.SalvarHistoricoRolagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RolarTesteLivreUseCaseImpl implements RolarTesteLivreUseCase {

    private final SalvarHistoricoRolagemPort salvarHistoricoRolagemPort;
    private final EventStorePort eventStorePort;

    public RolarTesteLivreUseCaseImpl(SalvarHistoricoRolagemPort salvarHistoricoRolagemPort,
                                      EventStorePort eventStorePort) {
        this.salvarHistoricoRolagemPort = salvarHistoricoRolagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public ResultadoRolagemQueryResponse executar(RolarDadosCommand command) {
        UUID rolagemId = UUID.randomUUID();
        
        ResultadoRolagemQueryResponse resultado = new ResultadoRolagemQueryResponse(
                rolagemId,
                command.personagemId(),
                command.formula(),
                18,
                "Rolagem: [15] + Modificador: [3]"
        );

        salvarHistoricoRolagemPort.salvar(resultado);

        DomainEvent eventoRolagem = new DomainEvent() {};
        eventStorePort.saveEvents(rolagemId.toString(), List.of(eventoRolagem), 0L);

        return resultado;
    }
}