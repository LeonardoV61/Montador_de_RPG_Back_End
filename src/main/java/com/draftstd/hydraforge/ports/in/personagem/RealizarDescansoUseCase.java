package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.ports.in.command.personagem.RealizarDescansoCommand;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;

public interface RealizarDescansoUseCase {
    PersonagemQueryResponse executar(RealizarDescansoCommand command);
}