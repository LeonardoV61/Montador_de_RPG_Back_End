package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.application.request.personagem.UsarItemConsumivelCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.personagem.UsarItemConsumivelUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.personagem.ObterPersonagemPort;
import com.draftstd.hydraforge.ports.out.persistence.personagem.SalvarPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsarItemConsumivelUseCaseImpl implements UsarItemConsumivelUseCase {

    private final ObterPersonagemPort obterPersonagemPort;
    private final SalvarPersonagemPort salvarPersonagemPort;
    private final EventStorePort eventStorePort;

    public UsarItemConsumivelUseCaseImpl(ObterPersonagemPort obterPersonagemPort, 
                                         SalvarPersonagemPort salvarPersonagemPort, 
                                         EventStorePort eventStorePort) {
        this.obterPersonagemPort = obterPersonagemPort;
        this.salvarPersonagemPort = salvarPersonagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public void executar(UsarItemConsumivelCommand command) {
        Personagem personagem = obterPersonagemPort.buscarPorId(command.personagemId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + command.personagemId()));

        long versaoAtual = personagem.getVersion();

        personagem.consumirItem(command.itemId(), command.sessaoId());

        eventStorePort.salvarEventos(personagem.getId().toString(), personagem.getUncommittedEvents(), versaoAtual);
        personagem.markEventsAsCommitted();

        salvarPersonagemPort.salvar(personagem);
    }
}