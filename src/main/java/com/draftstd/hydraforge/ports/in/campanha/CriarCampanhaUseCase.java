package com.draftstd.hydraforge.ports.in.campanha;

import com.draftstd.hydraforge.application.request.campanha.CriarCampanhaCommand;
import java.util.UUID;

public interface CriarCampanhaUseCase {
    UUID executar(CriarCampanhaCommand command);
}