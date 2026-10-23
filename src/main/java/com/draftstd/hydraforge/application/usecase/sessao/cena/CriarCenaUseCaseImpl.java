package com.draftstd.hydraforge.application.usecase.sessao.cena;

import com.draftstd.hydraforge.application.request.sessao.CriarCenaCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.sessao.CriarCenaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CriarCenaUseCaseImpl implements CriarCenaUseCase {

    private final CenaPersistencePort cenaPersistencePort;
    private final SessaoPersistencePort sessaoPersistencePort;

    public CriarCenaUseCaseImpl(CenaPersistencePort cenaPersistencePort, SessaoPersistencePort sessaoPersistencePort) {
        this.cenaPersistencePort = cenaPersistencePort;
        this.sessaoPersistencePort = sessaoPersistencePort;
    }

    @Override
    @Transactional
    public UUID executar(CriarCenaCommand command) {
        Sessao sessao = sessaoPersistencePort.buscarPorId(command.sessaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        UUID novoId = UUID.randomUUID();
        Cena cena = new Cena(
                novoId,
                command.sessaoId(),
                command.mapaJson(),
                command.urlMapa(),
                command.ordem(),
                command.tipo(),
                command.estadoJson()
        );

        sessao.adicionarCena(cena);
        cenaPersistencePort.salvar(cena);

        return novoId;
    }
}