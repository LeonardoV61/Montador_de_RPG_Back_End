package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.application.request.campanha.AtualizarCampanhaCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.AtualizarCampanhaUseCase;
import com.draftstd.hydraforge.ports.out.persistence.campanha.CampanhaPersistencePort;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AtualizarCampanhaUseCaseImpl implements AtualizarCampanhaUseCase {

    private final CampanhaPersistencePort campanhaPersistencePort;
    private final EventStorePort eventStorePort;

    public AtualizarCampanhaUseCaseImpl(CampanhaPersistencePort campanhaPersistencePort, EventStorePort eventStorePort) {
        this.campanhaPersistencePort = campanhaPersistencePort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public void executar(AtualizarCampanhaCommand command) {
        Campanha campanha = campanhaPersistencePort.buscarPorId(command.campanhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada com o ID: " + command.campanhaId()));

        campanha.atualizarInformacoes(command.nome(), command.descricao(), command.urlImagem());
        
        if (command.status() != null && !command.status().equals(campanha.getStatus())) {
            campanha.alterarStatus(command.status());
        }

        campanhaPersistencePort.salvar(campanha);
        eventStorePort.salvarEventos(campanha.getId().toString(), campanha.pullEventos(), campanha.getVersion());
    }
}