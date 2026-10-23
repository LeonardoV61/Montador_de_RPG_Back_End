package com.draftstd.hydraforge.application.usecase.sistema.entidade;

import com.draftstd.hydraforge.application.request.sistema.AtualizarEntidadeSistemaCommand;
import com.draftstd.hydraforge.application.request.sistema.CriarEntidadeSistemaCommand;
import com.draftstd.hydraforge.application.response.sistema.EntidadeSistemaQueryResponse;
import com.draftstd.hydraforge.application.response.sistema.ObterEntidadeSistemaQuery;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeSistema;
import com.draftstd.hydraforge.ports.in.sistema.GerenciarEntidadeSistemaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeSistemaOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.SalvarEntidadeSistemaPort;
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
    public UUID criar(CriarEntidadeSistemaCommand command) {
        UUID novoId = UUID.randomUUID();
        EntidadeSistema entidade = new EntidadeSistema(
            novoId,
            command.sistemaId(),
            command.tipo(),
            command.nome(),
            null,
            null,
            command.schema(),
            null
        );

        salvarEntidadeSistemaPort.salvar(entidade);
        return novoId;
    }

    @Override
    public void atualizar(AtualizarEntidadeSistemaCommand command) {
        EntidadeSistema entidade = entidadeSistemaOutputPort.buscarPorId(command.entidadeSistemaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Entidade do sistema não encontrada: " + command.entidadeSistemaId()));

        entidade.setNome(command.nome());
        entidade.setTipo(command.tipo());
        entidade.setAtributos(command.schema());

        salvarEntidadeSistemaPort.salvar(entidade);
    }

    @Override
    @Transactional(readOnly = true)
    public EntidadeSistemaQueryResponse obterPorId(ObterEntidadeSistemaQuery query) {
        EntidadeSistema entidade = entidadeSistemaOutputPort.buscarPorId(query.entidadeSistemaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Entidade do sistema não encontrada: " + query.entidadeSistemaId()));

        return new EntidadeSistemaQueryResponse(
                entidade.getId(),
                entidade.getSistemaId(),
                entidade.getNome(),
                entidade.getTipo(),
                entidade.getDescricao(),
                entidade.getUrlImagem(),
                entidade.getAtributos(),
                entidade.getPropriedades()
        );
    }
}