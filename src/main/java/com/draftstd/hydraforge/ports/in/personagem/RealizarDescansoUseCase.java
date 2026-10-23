package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.application.request.personagem.RealizarDescansoCommand;

public interface RealizarDescansoUseCase {
    void executar(RealizarDescansoCommand command);
}