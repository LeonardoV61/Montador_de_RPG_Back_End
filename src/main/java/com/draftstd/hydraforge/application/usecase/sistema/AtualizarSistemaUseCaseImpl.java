package com.draftstd.hydraforge.application.usecase.sistema;

import com.draftstd.hydraforge.application.request.sistema.AtualizarSistemaCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.ports.in.sistema.AtualizarSistemaUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ObterSistemaPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.SalvarSistemaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AtualizarSistemaUseCaseImpl implements AtualizarSistemaUseCase {

    private final ObterSistemaPort obterSistemaPort;
    private final SalvarSistemaPort salvarSistemaPort;
    private final EventStorePort eventStorePort;

    public AtualizarSistemaUseCaseImpl(ObterSistemaPort obterSistemaPort,
                                       SalvarSistemaPort salvarSistemaPort,
                                       EventStorePort eventStorePort) {
        this.obterSistemaPort = obterSistemaPort;
        this.salvarSistemaPort = salvarSistemaPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public void executar(AtualizarSistemaCommand command) {
        Sistema sistema = obterSistemaPort.buscarPorId(command.sistemaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema não encontrado: " + command.sistemaId()));

        long versaoAtual = sistema.getVersion();

        // Executa mutação de estado e aciona o SistemaAtualizadoEvent
        sistema.atualizar(
            command.nome(),
            command.descricao(),
            command.urlImagem(),
            command.configuracao(),
            command.schemaAtributos(),
            command.schemaEntidades(),
            command.schemaResolucoes()
        );

        // Dispara e grava no EventStore
        eventStorePort.salvarEventos(sistema.getId().toString(), sistema.getUncommittedEvents(), versaoAtual);
        sistema.markEventsAsCommitted();

        // Atualiza projeção relacional
        salvarSistemaPort.salvar(sistema);
    }
}