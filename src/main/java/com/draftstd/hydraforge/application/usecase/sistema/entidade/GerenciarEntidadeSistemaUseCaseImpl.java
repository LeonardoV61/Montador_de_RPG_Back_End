package com.draftstd.hydraforge.application.usecase.sistema.entidade;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeSistema;
import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarEntidadeSistemaCommand;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarEntidadeSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.EntidadeSistemaQueryResponse;
import com.draftstd.hydraforge.ports.in.sistema.GerenciarEntidadeSistemaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.entidade.EntidadeSistemaOutputPort;
import com.draftstd.hydraforge.ports.out.sistema.SalvarEntidadeSistemaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class GerenciarEntidadeSistemaUseCaseImpl implements GerenciarEntidadeSistemaUseCase {

    private final SalvarEntidadeSistemaPort salvarEntidadeSistemaPort;
    private final EntidadeSistemaOutputPort entidadeSistemaOutputPort;

    public GerenciarEntidadeSistemaUseCaseImpl(SalvarEntidadeSistemaPort salvarEntidadeSistemaPort,
                                               EntidadeSistemaOutputPort entidadeSistemaOutputPort) {
        this.salvarEntidadeSistemaPort = salvarEntidadeSistemaPort;
        this.entidadeSistemaOutputPort = entidadeSistemaOutputPort;
    }

    @Override
    public EntidadeSistemaQueryResponse criar(CriarEntidadeSistemaCommand command) {
        EntidadeSistema entidade = new EntidadeSistema();
        // O ID Long é gerado por sequence, não chamamos setId com UUID

        EntidadeSistema salva = salvarEntidadeSistemaPort.salvar(entidade);

        // Conversão segura do ID Long para UUID
        UUID entidadeUuid = salva.getId() != null 
                ? UUID.nameUUIDFromBytes(salva.getId().toString().getBytes()) 
                : UUID.randomUUID();

        String schemaStr = command.schemaJson() != null ? command.schemaJson().toString() : null;

        // Retorno preenchendo os 5 parâmetros exigidos pelo EntidadeSistemaQueryResponse
        return new EntidadeSistemaQueryResponse(
                entidadeUuid,
                command.sistemaId(),
                command.nome(),
                command.tipo(),
                schemaStr
        );
    }

    @Override
    public EntidadeSistemaQueryResponse atualizar(AtualizarEntidadeSistemaCommand command) {
        EntidadeSistema entidade = entidadeSistemaOutputPort.buscarPorId(command.entidadeSistemaId())
                .orElseThrow(() -> new RuntimeException("Entidade do sistema não encontrada: " + command.entidadeSistemaId()));

        EntidadeSistema atualizada = salvarEntidadeSistemaPort.salvar(entidade);

        UUID entidadeUuid = atualizada.getId() != null 
                ? UUID.nameUUIDFromBytes(atualizada.getId().toString().getBytes()) 
                : UUID.randomUUID();

        String schemaStr = command.schemaJson() != null ? command.schemaJson().toString() : null;

        return new EntidadeSistemaQueryResponse(
                entidadeUuid,
                null,
                command.nome(),
                command.tipo(),
                schemaStr
        );
    }

    @Override
    public EntidadeSistemaQueryResponse obterPorId(com.draftstd.hydraforge.ports.in.query.sistema.ObterEntidadeSistemaQuery query) {
        EntidadeSistema entidade = entidadeSistemaOutputPort.buscarPorId(query.entidadeSistemaId())
                .orElseThrow(() -> new RuntimeException("Entidade do sistema não encontrada: " + query.entidadeSistemaId()));

        UUID entidadeUuid = entidade.getId() != null 
                ? UUID.nameUUIDFromBytes(entidade.getId().toString().getBytes()) 
                : UUID.randomUUID();

        return new EntidadeSistemaQueryResponse(
                entidadeUuid,
                null,
                null,
                null,
                null
        );
    }
}