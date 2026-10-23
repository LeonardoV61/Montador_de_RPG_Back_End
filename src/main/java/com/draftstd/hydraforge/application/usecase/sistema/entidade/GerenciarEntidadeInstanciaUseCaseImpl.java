package com.draftstd.hydraforge.application.usecase.sistema.entidade;

import com.draftstd.hydraforge.application.request.sistema.AtualizarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.application.request.sistema.CriarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.application.response.sistema.EntidadeInstanciaQueryResponse;
import com.draftstd.hydraforge.application.response.sistema.ObterEntidadeInstanciaQuery;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.ports.in.sistema.GerenciarEntidadeInstanciaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.SalvarEntidadeInstanciaPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public UUID criar(CriarEntidadeInstanciaCommand command) {
        UUID novoId = UUID.randomUUID();
        EntidadeInstancia instancia = new EntidadeInstancia(
            novoId,
            null,
            command.entidadeSistemaId(),
            command.donoId(),
            null,
            command.nome(),
            null,
            null,
            command.valoresAtributos(),
            null,
            LocalDateTime.now()
        );

        salvarEntidadeInstanciaPort.salvar(instancia);
        return novoId;
    }

    @Override
    public void atualizar(AtualizarEntidadeInstanciaCommand command) {
        EntidadeInstancia instancia = entidadeInstanciaOutputPort.buscarPorId(command.instanciaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Entidade instância não encontrada: " + command.instanciaId()));

        instancia.setNome(command.nome());
        instancia.setValoresAtributos(command.valoresAtributos());

        salvarEntidadeInstanciaPort.salvar(instancia);
    }

    @Override
    @Transactional(readOnly = true)
    public EntidadeInstanciaQueryResponse obterPorId(ObterEntidadeInstanciaQuery query) {
        EntidadeInstancia instancia = entidadeInstanciaOutputPort.buscarPorId(query.instanciaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Entidade instância não encontrada: " + query.instanciaId()));

        return new EntidadeInstanciaQueryResponse(
                instancia.getId(),
                instancia.getCampanhaId(),
                instancia.getEntidadeSistemaId(),
                instancia.getDonoId(),
                instancia.getTipo(),
                instancia.getNome(),
                instancia.getDescricao(),
                instancia.getUrlImagem(),
                instancia.getValoresAtributos(),
                instancia.getCustomizacoes(),
                instancia.getCriadaEm()
        );
    }
}