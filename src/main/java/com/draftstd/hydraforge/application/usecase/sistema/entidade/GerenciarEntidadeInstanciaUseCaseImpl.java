package com.draftstd.hydraforge.application.usecase.sistema.entidade;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.EntidadeInstanciaQueryResponse;
import com.draftstd.hydraforge.ports.in.sistema.entidade.GerenciarEntidadeInstanciaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.ports.out.sistema.SalvarEntidadeInstanciaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class GerenciarEntidadeInstanciaUseCaseImpl implements GerenciarEntidadeInstanciaUseCase {

    private final SalvarEntidadeInstanciaPort salvarEntidadeInstanciaPort;
    private final EntidadeInstanciaOutputPort entidadeInstanciaOutputPort;

    public GerenciarEntidadeInstanciaUseCaseImpl(SalvarEntidadeInstanciaPort salvarEntidadeInstanciaPort,
                                                EntidadeInstanciaOutputPort entidadeInstanciaOutputPort) {
        this.salvarEntidadeInstanciaPort = salvarEntidadeInstanciaPort;
        this.entidadeInstanciaOutputPort = entidadeInstanciaOutputPort;
    }

    @Override
    public EntidadeInstanciaQueryResponse criar(CriarEntidadeInstanciaCommand command) {
        EntidadeInstancia instancia = new EntidadeInstancia();
        instancia.setId(UUID.randomUUID());
        instancia.setEntidadeSistemaId(command.entidadeSistemaId());
        instancia.setValoresAtributos(command.valoresAtributos());

        EntidadeInstancia salva = salvarEntidadeInstanciaPort.salvar(instancia);

        return new EntidadeInstanciaQueryResponse(
                salva.getId(),
                salva.getEntidadeSistemaId(),
                salva.getValoresAtributos()
        );
    }

    @Override
    public EntidadeInstanciaQueryResponse atualizar(AtualizarEntidadeInstanciaCommand command) {
        EntidadeInstancia instancia = entidadeInstanciaOutputPort.buscarPorId(command.instanciaId());
        instancia.setValoresAtributos(command.valoresAtributos());

        EntidadeInstancia atualizada = salvarEntidadeInstanciaPort.salvar(instancia);

        return new EntidadeInstanciaQueryResponse(
                atualizada.getId(),
                atualizada.getEntidadeSistemaId(),
                atualizada.getValoresAtributos()
        );
    }
}