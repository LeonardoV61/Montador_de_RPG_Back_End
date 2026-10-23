package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.application.request.campanha.AdicionarJogadorCampanhaCommand;
import com.draftstd.hydraforge.application.request.campanha.AlterarPapelJogadorCommand;
import com.draftstd.hydraforge.application.request.campanha.RemoverJogadorCampanhaCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.GerenciarParticipantesCampanhaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.campanha.CampanhaPersistencePort;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class GerenciarParticipantesCampanhaUseCaseImpl implements GerenciarParticipantesCampanhaUseCase {

    private final CampanhaPersistencePort campanhaPersistencePort;
    private final EventStorePort eventStorePort;

    public GerenciarParticipantesCampanhaUseCaseImpl(CampanhaPersistencePort campanhaPersistencePort, EventStorePort eventStorePort) {
        this.campanhaPersistencePort = campanhaPersistencePort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public void adicionarJogador(AdicionarJogadorCampanhaCommand command) {
        Campanha campanha = buscarCampanha(command.campanhaId());
        campanha.adicionarMembro(command.usuarioId(), command.papel(), command.statusMembro());
        salvarEEmitirEventos(campanha);
    }

    @Override
    public void removerJogador(RemoverJogadorCampanhaCommand command) {
        Campanha campanha = buscarCampanha(command.campanhaId());
        campanha.removerMembro(command.usuarioId());
        salvarEEmitirEventos(campanha);
    }

    @Override
    public void alterarPapel(AlterarPapelJogadorCommand command) {
        Campanha campanha = buscarCampanha(command.campanhaId());
        if (command.novoStatus() != null) {
            campanha.alterarStatusMembro(command.usuarioId(), command.novoStatus());
        }
        salvarEEmitirEventos(campanha);
    }

    private Campanha buscarCampanha(UUID campanhaId) {
        return campanhaPersistencePort.buscarPorId(campanhaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada com o ID: " + campanhaId));
    }

    private void salvarEEmitirEventos(Campanha campanha) {
        campanhaPersistencePort.salvar(campanha);
        eventStorePort.salvarEventos(campanha.getId().toString(), campanha.pullEventos(), campanha.getVersion());
    }
}