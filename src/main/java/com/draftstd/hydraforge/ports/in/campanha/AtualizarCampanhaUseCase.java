package com.draftstd.hydraforge.ports.in.campanha;

import com.draftstd.hydraforge.application.request.campanha.AtualizarCampanhaCommand;

public interface AtualizarCampanhaUseCase {
    void executar(AtualizarCampanhaCommand command);
}