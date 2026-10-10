package com.draftstd.hydraforge.application.usecase.sessao.anotacao;

import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.ports.in.command.sessao.CriarAnotacaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.CriarAnotacaoUseCase;
import com.draftstd.hydraforge.ports.out.sessao.SalvarAnotacaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Transactional
public class CriarAnotacaoUseCaseImpl implements CriarAnotacaoUseCase {

    private final SalvarAnotacaoPort salvarAnotacaoPort;

    public CriarAnotacaoUseCaseImpl(SalvarAnotacaoPort salvarAnotacaoPort) {
        this.salvarAnotacaoPort = salvarAnotacaoPort;
    }

    @Override
    public AnotacaoQueryResponse executar(CriarAnotacaoCommand command) {
        Anotacao anotacao = new Anotacao();
        anotacao.setId(UUID.randomUUID());
        anotacao.setSessaoId(command.sessaoId());
        anotacao.setAutorId(command.autorId());
        anotacao.setTitulo(command.titulo());
        anotacao.setConteudo(command.conteudo());
        anotacao.setPrivada(command.privada());
        anotacao.setCriadaEm(OffsetDateTime.now());

        Anotacao salva = salvarAnotacaoPort.salvar(anotacao);

        return new AnotacaoQueryResponse(
                salva.getId(),
                salva.getSessaoId(),
                salva.getAutorId(),
                salva.getTitulo(),
                salva.getConteudo(),
                salva.isPrivada(),
                salva.getCriadaEm()
        );
    }
}