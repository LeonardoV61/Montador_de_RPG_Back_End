package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.ports.in.command.personagem.CriarPersonagemCommand;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;

public interface CriarPersonagemUseCase {
    PersonagemQueryResponse executar(CriarPersonagemCommand command);
}