package com.draftstd.hydraforge.ports.in.campanha;

import com.draftstd.hydraforge.ports.in.command.campanha.AtualizarCampanhaCommand;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;

public interface AtualizarCampanhaUseCase {
    CampanhaQueryResponse executar(AtualizarCampanhaCommand command);
}