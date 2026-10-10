package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.ports.in.command.personagem.AtualizarPersonagemCommand;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;

public interface AtualizarPersonagemUseCase {
    PersonagemQueryResponse executar(AtualizarPersonagemCommand command);
}