package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.application.request.personagem.CriarPersonagemCommand;
import java.util.UUID;

public interface CriarPersonagemUseCase {
    UUID executar(CriarPersonagemCommand command);
}