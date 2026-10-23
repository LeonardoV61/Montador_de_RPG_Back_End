package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.application.request.sistema.CriarSistemaCommand;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.ports.in.sistema.CriarSistemaUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.SalvarSistemaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CriarSistemaUseCaseImpl implements CriarSistemaUseCase {

    private final SalvarSistemaPort salvarSistemaPort;
    private final EventStorePort eventStorePort;

    public CriarSistemaUseCaseImpl(SalvarSistemaPort salvarSistemaPort, EventStorePort eventStorePort) {
        this.salvarSistemaPort = salvarSistemaPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public UUID executar(CriarSistemaCommand command) {
        UUID novoId = UUID.randomUUID();

        // Instancia o Agregado invocando o construtor que grava o SistemaCriadoEvent
        Sistema sistema = new Sistema(
            novoId,
            command.usuarioCriadorId(),
            command.sistemaPaiId(),
            command.nome(),
            command.descricao(),
            command.urlImagem(),
            command.configuracao(),
            command.schemaAtributos(),
            command.schemaEntidades(),
            command.schemaResolucoes(),
            command.eOficial()
        );

        // Grava eventos no EventStore
        eventStorePort.salvarEventos(novoId.toString(), sistema.getUncommittedEvents(), 0L);
        sistema.markEventsAsCommitted();

        // Atualiza a projeção de leitura relacional
        salvarSistemaPort.salvar(sistema);

        return novoId;
    }
}