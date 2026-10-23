package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.application.request.personagem.UsarItemConsumivelCommand;

public interface UsarItemConsumivelUseCase {
    void executar(UsarItemConsumivelCommand command);
}