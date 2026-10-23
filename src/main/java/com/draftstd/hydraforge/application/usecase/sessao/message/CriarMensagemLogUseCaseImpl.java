package com.draftstd.hydraforge.application.usecase.sessao.message;

import com.draftstd.hydraforge.application.request.sessao.CriarMensagemLogCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.MensagemLog;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.sessao.CriarMensagemLogUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.MensagemLogPersistencePort;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class CriarMensagemLogUseCaseImpl implements CriarMensagemLogUseCase {

    private final MensagemLogPersistencePort mensagemLogPersistencePort;
    private final SessaoPersistencePort sessaoPersistencePort;

    public CriarMensagemLogUseCaseImpl(MensagemLogPersistencePort mensagemLogPersistencePort, SessaoPersistencePort sessaoPersistencePort) {
        this.mensagemLogPersistencePort = mensagemLogPersistencePort;
        this.sessaoPersistencePort = sessaoPersistencePort;
    }

    @Override
    public UUID executar(CriarMensagemLogCommand command) {
        Sessao sessao = sessaoPersistencePort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        UUID novoId = UUID.randomUUID();
        MensagemLog mensagem = new MensagemLog(
                novoId,
                command.emissorId(),
                command.conteudo(),
                LocalDateTime.now()
        );

        sessao.adicionarMensagem(command.emissorId(), command.conteudo());
        mensagemLogPersistencePort.salvar(sessao.getId(), mensagem);

        return novoId;
    }
}