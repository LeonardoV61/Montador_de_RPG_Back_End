package com.draftstd.hydraforge.usecase;

import com.draftstd.hydraforge.config.IntegrationTestBase;
import com.draftstd.hydraforge.application.request.sessao.CriarSessaoCommand;
import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import com.draftstd.hydraforge.ports.in.sessao.CriarSessaoUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        // Given: Persistir uma Campanha válida previamente com todos os campos obrigatórios para satisfazer a FK
        UUID campanhaId = UUID.randomUUID();
        var campanhaJpa = new com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaJpaEntity();
        campanhaJpa.setId(campanhaId);
        campanhaJpa.setNome("Campanha de Teste");
        campanhaJpa.setMestreId(UUID.randomUUID());
        campanhaJpa.setSistemaId(UUID.randomUUID());
        campanhaJpa.setStatus(StatusCampanha.ATIVA);
        campanhaJpa.setVersion(0L);

        em.persist(campanhaJpa);
        em.flush();

        CriarSessaoCommand command = new CriarSessaoCommand(
                campanhaId, 
                "Sessao de Teste Event Sourcing", 
                1, 
                LocalDateTime.now().plusDays(1)
        );

        // When
        UUID sessaoId = criarSessaoUseCase.executar(command);

        // Sincronizar e limpar cache para forçar a leitura correta do banco de dados
        flushAndClear();

        // Then
        assertNotNull(sessaoId);
        var eventos = eventStorePort.carregarEventosPorSessao(sessaoId);
        assertNotNull(eventos);
        org.junit.jupiter.api.Assertions.assertTrue(eventos.size() > 0, "Deverá conter pelo menos um evento persistido na EventStore");
    }
}