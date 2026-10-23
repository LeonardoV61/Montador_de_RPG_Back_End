package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.application.request.personagem.AtualizarPersonagemCommand;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.personagem.AtualizarPersonagemUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.persistence.personagem.ObterPersonagemPort;
import com.draftstd.hydraforge.ports.out.persistence.personagem.SalvarPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AtualizarPersonagemUseCaseImpl implements AtualizarPersonagemUseCase {

    private final ObterPersonagemPort obterPersonagemPort;
    private final SalvarPersonagemPort salvarPersonagemPort;
    private final EventStorePort eventStorePort;

    public AtualizarPersonagemUseCaseImpl(ObterPersonagemPort obterPersonagemPort, 
                                          SalvarPersonagemPort salvarPersonagemPort, 
                                          EventStorePort eventStorePort) {
        this.obterPersonagemPort = obterPersonagemPort;
        this.salvarPersonagemPort = salvarPersonagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public void executar(AtualizarPersonagemCommand command) {
        Personagem personagem = obterPersonagemPort.buscarPorId(command.personagemId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + command.personagemId()));

        long versaoAtual = personagem.getVersion();

        // Executa mutação via comportamento do Agregado
        personagem.atualizar(
            command.nome(),
            command.historia(),
            command.aparencia(),
            command.urlImagem(),
            command.notasJogador(),
            command.atributos(),
            command.sessaoId()
        );

        eventStorePort.salvarEventos(personagem.getId().toString(), personagem.getUncommittedEvents(), versaoAtual);
        personagem.markEventsAsCommitted();

        salvarPersonagemPort.salvar(personagem);
    }
}