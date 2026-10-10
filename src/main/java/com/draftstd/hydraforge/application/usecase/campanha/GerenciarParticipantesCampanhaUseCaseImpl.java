package com.draftstd.hydraforge.application.usecase.campanha;

import com.draftstd.hydraforge.domain.events.DomainEvent; // Substituir pelas classes concretas de evento correspondentes
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.in.campanha.GerenciarParticipantesCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.command.campanha.AdicionarJogadorCampanhaCommand;
import com.draftstd.hydraforge.ports.in.command.campanha.AlterarPapelJogadorCommand;
import com.draftstd.hydraforge.ports.in.command.campanha.RemoverJogadorCampanhaCommand;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.ports.out.campanha.ObterCampanhaPort;
import com.draftstd.hydraforge.ports.out.campanha.SalvarCampanhaPort;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GerenciarParticipantesCampanhaUseCaseImpl implements GerenciarParticipantesCampanhaUseCase {

    private final ObterCampanhaPort obterCampanhaPort;
    private final SalvarCampanhaPort salvarCampanhaPort;
    private final EventStorePort eventStorePort;

    public GerenciarParticipantesCampanhaUseCaseImpl(ObterCampanhaPort obterCampanhaPort,
                                                    SalvarCampanhaPort salvarCampanhaPort,
                                                    EventStorePort eventStorePort) {
        this.obterCampanhaPort = obterCampanhaPort;
        this.salvarCampanhaPort = salvarCampanhaPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public CampanhaQueryResponse adicionarJogador(AdicionarJogadorCampanhaCommand command) {
        Campanha campanha = obterCampanhaPort.buscarPorId(command.campanhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada: " + command.campanhaId()));

        Campanha salva = salvarCampanhaPort.salvar(campanha);

        DomainEvent eventoAdicionado = new DomainEvent() {};
        eventStorePort.saveEvents(
                salva.getId().toString(),
                List.of(eventoAdicionado),
                salva.getVersao() != null ? salva.getVersao() : 1L
        );

        return toQueryResponse(salva);
    }

    @Override
    public CampanhaQueryResponse removerJogador(RemoverJogadorCampanhaCommand command) {
        Campanha campanha = obterCampanhaPort.buscarPorId(command.campanhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada: " + command.campanhaId()));

        Campanha salva = salvarCampanhaPort.salvar(campanha);

        DomainEvent eventoRemovido = new DomainEvent() {};
        eventStorePort.saveEvents(
                salva.getId().toString(),
                List.of(eventoRemovido),
                salva.getVersao() != null ? salva.getVersao() : 1L
        );

        return toQueryResponse(salva);
    }

    @Override
    public CampanhaQueryResponse alterarPapel(AlterarPapelJogadorCommand command) {
        Campanha campanha = obterCampanhaPort.buscarPorId(command.campanhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Campanha não encontrada: " + command.campanhaId()));

        Campanha salva = salvarCampanhaPort.salvar(campanha);

        DomainEvent eventoPapelAlterado = new DomainEvent() {};
        eventStorePort.saveEvents(
                salva.getId().toString(),
                List.of(eventoPapelAlterado),
                salva.getVersao() != null ? salva.getVersao() : 1L
        );

        return toQueryResponse(salva);
    }

    private CampanhaQueryResponse toQueryResponse(Campanha c) {
        return new CampanhaQueryResponse(
                c.getId(),
                c.getTitulo(),
                c.getDescricao(),
                c.getSistemaId(),
                c.getMestreId(),
                c.getStatus(),
                c.getCriadaEm()
        );
    }
}