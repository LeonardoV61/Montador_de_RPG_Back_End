package com.draftstd.hydraforge.application.usecase.sistema.entidade;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.EntidadeInstanciaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sistema.ObterEntidadeInstanciaQuery;
import com.draftstd.hydraforge.ports.in.sistema.GerenciarEntidadeInstanciaUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.entidade.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.ports.out.sistema.SalvarEntidadeInstanciaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GerenciarEntidadeInstanciaUseCaseImpl implements GerenciarEntidadeInstanciaUseCase {

    private final SalvarEntidadeInstanciaPort salvarEntidadeInstanciaPort;
    private final EntidadeInstanciaOutputPort entidadeInstanciaOutputPort;
    private final EventStorePort eventStorePort;

    public GerenciarEntidadeInstanciaUseCaseImpl(SalvarEntidadeInstanciaPort salvarEntidadeInstanciaPort,
                                                EntidadeInstanciaOutputPort entidadeInstanciaOutputPort,
                                                EventStorePort eventStorePort) {
        this.salvarEntidadeInstanciaPort = salvarEntidadeInstanciaPort;
        this.entidadeInstanciaOutputPort = entidadeInstanciaOutputPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public EntidadeInstanciaQueryResponse criar(CriarEntidadeInstanciaCommand command) {
        EntidadeInstancia instancia = new EntidadeInstancia();
        
        EntidadeInstancia salva = salvarEntidadeInstanciaPort.salvar(instancia);

        UUID instanciaUuid = salva.getId() != null 
                ? UUID.nameUUIDFromBytes(salva.getId().toString().getBytes()) 
                : UUID.randomUUID();

        String valoresJsonStr = command.valoresAtributosJson() != null 
                ? command.valoresAtributosJson().toString() 
                : null;

        eventStorePort.saveEvents(instanciaUuid.toString(), List.of(), 0L);

        return new EntidadeInstanciaQueryResponse(
                instanciaUuid,
                command.entidadeSistemaId(),
                command.donoId(),
                command.nome(),
                valoresJsonStr,
                LocalDateTime.now()
        );
    }

    @Override
    public EntidadeInstanciaQueryResponse atualizar(AtualizarEntidadeInstanciaCommand command) {
        EntidadeInstancia instancia = entidadeInstanciaOutputPort.buscarPorId(command.instanciaId())
                .orElseThrow(() -> new RuntimeException("Entidade instância não encontrada: " + command.instanciaId()));
        
        EntidadeInstancia atualizada = salvarEntidadeInstanciaPort.salvar(instancia);
        
        UUID instanciaUuid = atualizada.getId() != null 
                ? UUID.nameUUIDFromBytes(atualizada.getId().toString().getBytes()) 
                : UUID.randomUUID();

        String valoresJsonStr = command.valoresAtributosJson() != null 
                ? command.valoresAtributosJson().toString() 
                : null;

        eventStorePort.saveEvents(instanciaUuid.toString(), List.of(), 0L);

        return new EntidadeInstanciaQueryResponse(
                instanciaUuid,
                null,
                null,
                command.nome(),
                valoresJsonStr,
                LocalDateTime.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EntidadeInstanciaQueryResponse obterPorId(ObterEntidadeInstanciaQuery query) {
        EntidadeInstancia instancia = entidadeInstanciaOutputPort.buscarPorId(query.instanciaId())
                .orElseThrow(() -> new RuntimeException("Entidade instância não encontrada: " + query.instanciaId()));

        UUID instanciaUuid = instancia.getId() != null 
                ? UUID.nameUUIDFromBytes(instancia.getId().toString().getBytes()) 
                : UUID.randomUUID();

        return new EntidadeInstanciaQueryResponse(
                instanciaUuid,
                null,
                null,
                null,
                null,
                LocalDateTime.now()
        );
    }
}