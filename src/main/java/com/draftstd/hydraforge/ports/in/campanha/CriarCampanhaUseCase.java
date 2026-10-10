package com.draftstd.hydraforge.ports.in.campanha;

import com.draftstd.hydraforge.ports.in.command.campanha.CriarCampanhaCommand;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;

public interface CriarCampanhaUseCase {
    CampanhaQueryResponse executar(CriarCampanhaCommand command);
}