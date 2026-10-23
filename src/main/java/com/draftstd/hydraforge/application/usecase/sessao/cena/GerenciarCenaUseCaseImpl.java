package com.draftstd.hydraforge.application.usecase.sessao.cena;

import com.draftstd.hydraforge.application.request.sessao.AtualizarCenaCommand;
import com.draftstd.hydraforge.application.request.sessao.CriarCenaCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarCenaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class GerenciarCenaUseCaseImpl implements GerenciarCenaUseCase {

    private final CenaPersistencePort cenaPersistencePort;

    public GerenciarCenaUseCaseImpl(CenaPersistencePort cenaPersistencePort) {
        this.cenaPersistencePort = cenaPersistencePort;
    }

    @Override
    public UUID criarCena(CriarCenaCommand command) {
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

        cenaPersistencePort.salvar(cena);
        return novoId;
    }

    @Override
    public void atualizarCena(AtualizarCenaCommand command) {
        Cena cena = cenaPersistencePort.buscarPorId(command.cenaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cena não encontrada: " + command.cenaId()));

        cena.atualizarEstado(
                "", // Titulo permanece inalterado se vazio
                command.mapaJson(),
                command.urlMapa(),
                command.ordem(),
                command.tipo(),
                command.estadoJson(),
                cena.getAtiva()
        );

        cenaPersistencePort.salvar(cena);
    }
}