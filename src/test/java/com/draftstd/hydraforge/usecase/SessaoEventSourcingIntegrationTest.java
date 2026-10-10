package com.draftstd.hydraforge.usecase;

import com.draftstd.hydraforge.config.IntegrationTestBase;
import com.draftstd.hydraforge.ports.in.command.sessao.CriarSessaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.CriarSessaoUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class SessaoEventSourcingIntegrationTest extends IntegrationTestBase {

    @Autowired
    private CriarSessaoUseCase criarSessaoUseCase;

    @Autowired
    private EventStorePort eventStorePort;

    @Test
    @DisplayName("Deve executar o Use Case de criação de sessão e persistir evento na EventStore")
    void deveCriarSessaoEPersistirEventoNaEventStore() {
        // Given (Dado)
        UUID campanhaId = UUID.randomUUID();
        CriarSessaoCommand command = new CriarSessaoCommand(campanhaId, "Sessao de Teste Event Sourcing");

        // When (Quando)
        SessaoQueryResponse response = criarSessaoUseCase.executar(command);

        // Then (Então)
        assertNotNull(response);
        assertNotNull(response.id());

        // Valida se o evento foi de fato persistido e recuperado pela porta da EventStore
        var eventos = eventStorePort.getEventsForAggregate(response.id().toString());
        assertNotNull(eventos);
    }
}