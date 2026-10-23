package com.draftstd.hydraforge.application.usecase.mecanica;

import com.draftstd.hydraforge.application.request.mecanica.RolarDadosCommand;
import com.draftstd.hydraforge.application.response.mecanica.ResultadoRolagemQueryResponse;
import com.draftstd.hydraforge.domain.events.mecanica.TesteLivreRoladoEvent;
import com.draftstd.hydraforge.ports.in.mecanica.RolarDadosUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.mecanica.SalvarHistoricoRolagemPort;
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

        // 1. Validação de Segurança e Cálculo do Total no Backend
        int somaDados = command.dadosSorteados().stream().mapToInt(Integer::intValue).sum();
        int resultadoFinal = somaDados + command.modificador();

        // 2. Montagem do DTO da Rolagem com a data atual
        ResultadoRolagemQueryResponse resultado = new ResultadoRolagemQueryResponse(
                rolagemId,
                command.sessaoId(),
                command.jogadorId(),
                command.personagemId(),
                command.expressaoDados(),
                command.dadosSorteados(),
                command.modificador(),
                resultadoFinal,
                command.motivo(),
                LocalDateTime.now()
        );

        // 3. Persistência do Histórico
        salvarHistoricoRolagemPort.salvar(resultado);

        // 4. Emissão e Registro do Evento de Domínio no EventStore
        TesteLivreRoladoEvent evento = new TesteLivreRoladoEvent(
                UUID.randomUUID(),
                rolagemId.toString(),
                command.personagemId(), // Corrigido para passar UUID
                command.expressaoDados(),
                resultadoFinal,
                1L,
                Instant.now()
        );
        eventStorePort.salvarEventos(rolagemId.toString(), List.of(evento), 0L);

        return resultado;
    }
}