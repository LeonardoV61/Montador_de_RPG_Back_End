package com.draftstd.hydraforge.ports.in.importacao;

import com.draftstd.hydraforge.ports.in.command.importacao.ImportarPersonagemCommand;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;

public interface ImportarPersonagemUseCase {
    PersonagemQueryResponse executar(ImportarPersonagemCommand command);
}