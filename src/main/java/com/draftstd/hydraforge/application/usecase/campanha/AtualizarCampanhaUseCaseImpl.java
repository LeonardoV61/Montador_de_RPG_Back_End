package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.domain.events.DomainEvent; // Substituir pela classe concreta ex: CampanhaAtualizadaEvent
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.AtualizarCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.command.campanha.AtualizarCampanhaCommand;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.ports.out.campanha.ObterCampanhaPort;
import com.draftstd.hydraforge.ports.out.campanha.SalvarCampanhaPort;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AtualizarCampanhaUseCaseImpl implements AtualizarCampanhaUseCase {

    private final ObterCampanhaPort obterCampanhaPort;
    private final SalvarCampanhaPort salvarCampanhaPort;
    private final EventStorePort eventStorePort;

    public AtualizarCampanhaUseCaseImpl(ObterCampanhaPort obterCampanhaPort,
                                       SalvarCampanhaPort salvarCampanhaPort,
                                       EventStorePort eventStorePort) {
        this.obterCampanhaPort = obterCampanhaPort;
        this.salvarCampanhaPort = salvarCampanhaPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public CampanhaQueryResponse executar(AtualizarCampanhaCommand command) {
        Campanha campanha = obterCampanhaPort.buscarPorId(command.campanhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada: " + command.campanhaId()));

        campanha.setTitulo(command.titulo());
        campanha.setDescricao(command.descricao());
        if (command.status() != null) {
            campanha.setStatus(command.status());
        }

        Campanha atualizada = salvarCampanhaPort.salvar(campanha);

        DomainEvent eventoAtualizado = new DomainEvent() {};
        eventStorePort.saveEvents(
                atualizada.getId().toString(),
                List.of(eventoAtualizado),
                atualizada.getVersao() != null ? atualizada.getVersao() : 1L
        );

        return new CampanhaQueryResponse(
                atualizada.getId(),
                atualizada.getTitulo(),
                atualizada.getDescricao(),
                atualizada.getSistemaId(),
                atualizada.getMestreId(),
                atualizada.getStatus(),
                atualizada.getCriadaEm()
        );
    }
}