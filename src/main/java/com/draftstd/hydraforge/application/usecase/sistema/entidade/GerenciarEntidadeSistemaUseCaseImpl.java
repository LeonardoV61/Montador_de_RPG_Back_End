package com.draftstd.hydraforge.application.usecase.sistema.entidade;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeSistema;
import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarEntidadeSistemaCommand;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarEntidadeSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.EntidadeSistemaQueryResponse;
import com.draftstd.hydraforge.ports.in.sistema.entidade.GerenciarEntidadeSistemaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeSistemaOutputPort;
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
        entidade.setId(UUID.randomUUID());
        entidade.setSistemaId(command.sistemaId());
        entidade.setNome(command.nome());
        entidade.setTipo(command.tipo());

        EntidadeSistema salva = salvarEntidadeSistemaPort.salvar(entidade);

        return new EntidadeSistemaQueryResponse(
                salva.getId(),
                salva.getSistemaId(),
                salva.getNome(),
                salva.getTipo()
        );
    }

    @Override
    public EntidadeSistemaQueryResponse atualizar(AtualizarEntidadeSistemaCommand command) {
        EntidadeSistema entidade = entidadeSistemaOutputPort.buscarPorId(command.entidadeId());
        entidade.setNome(command.nome());
        entidade.setTipo(command.tipo());

        EntidadeSistema atualizada = salvarEntidadeSistemaPort.salvar(entidade);

        return new EntidadeSistemaQueryResponse(
                atualizada.getId(),
                atualizada.getSistemaId(),
                atualizada.getNome(),
                atualizada.getTipo()
        );
    }
}