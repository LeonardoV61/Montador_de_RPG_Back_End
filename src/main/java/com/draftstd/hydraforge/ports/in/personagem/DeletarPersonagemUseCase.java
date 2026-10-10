package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.ports.in.command.personagem.DeletarPersonagemCommand;

public interface DeletarPersonagemUseCase {
    void executar(DeletarPersonagemCommand command);
}