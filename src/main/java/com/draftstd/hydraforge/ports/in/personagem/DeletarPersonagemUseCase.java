package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.application.request.personagem.DeletarPersonagemCommand;

public interface DeletarPersonagemUseCase {
    void executar(DeletarPersonagemCommand command);
}