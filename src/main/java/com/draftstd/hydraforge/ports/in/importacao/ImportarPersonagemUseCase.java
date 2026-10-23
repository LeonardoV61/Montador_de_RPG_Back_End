package com.draftstd.hydraforge.ports.in.importacao;

import com.draftstd.hydraforge.application.request.importacao.ImportarPersonagemCommand;
import com.draftstd.hydraforge.application.response.personagem.PersonagemQueryResponse;

public interface ImportarPersonagemUseCase {
    PersonagemQueryResponse executar(ImportarPersonagemCommand command);
}