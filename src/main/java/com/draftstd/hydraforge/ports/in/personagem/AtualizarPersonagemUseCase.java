package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.application.request.personagem.AtualizarPersonagemCommand;

public interface AtualizarPersonagemUseCase {
    void executar(AtualizarPersonagemCommand command);
}