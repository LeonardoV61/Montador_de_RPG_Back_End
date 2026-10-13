package com.draftstd.hydraforge.application.usecase.sessao.anotacao;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.ports.in.command.sessao.AtualizarAnotacaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarAnotacaoUseCase;
import com.draftstd.hydraforge.ports.out.sessao.ObterAnotacaoPort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarAnotacaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class GerenciarAnotacaoUseCaseImpl implements GerenciarAnotacaoUseCase {

    private final ObterAnotacaoPort obterAnotacaoPort;
    private final SalvarAnotacaoPort salvarAnotacaoPort;

    public GerenciarAnotacaoUseCaseImpl(ObterAnotacaoPort obterAnotacaoPort, SalvarAnotacaoPort salvarAnotacaoPort) {
        this.obterAnotacaoPort = obterAnotacaoPort;
        this.salvarAnotacaoPort = salvarAnotacaoPort;
    }

    @Override
    public AnotacaoQueryResponse atualizar(AtualizarAnotacaoCommand command) {
        Anotacao anotacao = obterAnotacaoPort.buscarPorId(command.anotacaoId()).orElseThrow(() -> new RecursoNaoEncontradoException("Anotação não encontrada: " + command.anotacaoId()));

        anotacao.setTitulo(command.titulo());
        anotacao.setConteudo(command.conteudo());
        
        if (command.privada() != null) {
            anotacao.setEPrivado(command.privada());
        }

        Anotacao atualizada = salvarAnotacaoPort.salvar(anotacao);

        // Conversão segura do ID Integer para UUID
        UUID anotacaoUuid = atualizada.getIdAnotacao() != null ? UUID.nameUUIDFromBytes(atualizada.getIdAnotacao().toString().getBytes()) : UUID.randomUUID();

        // Extração de IDs relacionais se aplicável
        UUID sessaoId = (atualizada.getSessao() != null) ? null : null; // Ajustar conforme os getters da Sessão/Usuario se existirem
        UUID autorId = (atualizada.getUsuario() != null) ? null : null;

        LocalDateTime agora = LocalDateTime.now();

        return new AnotacaoQueryResponse(anotacaoUuid, sessaoId, autorId, atualizada.getTitulo(), atualizada.getConteudo(), atualizada.getEPrivado() != null ? atualizada.getEPrivado() : true, agora, agora);
    }
}