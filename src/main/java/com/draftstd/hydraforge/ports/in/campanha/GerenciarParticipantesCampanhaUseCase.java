package com.draftstd.hydraforge.ports.in.campanha;

import com.draftstd.hydraforge.ports.in.command.campanha.AdicionarJogadorCampanhaCommand;
import com.draftstd.hydraforge.ports.in.command.campanha.AlterarPapelJogadorCommand;
import com.draftstd.hydraforge.ports.in.command.campanha.RemoverJogadorCampanhaCommand;
import com.draftstd.hydraforge.ports.in.query.campanha.CampanhaQueryResponse;

public interface GerenciarParticipantesCampanhaUseCase {
    CampanhaQueryResponse adicionarJogador(AdicionarJogadorCampanhaCommand command);
    CampanhaQueryResponse removerJogador(RemoverJogadorCampanhaCommand command);
    CampanhaQueryResponse alterarPapel(AlterarPapelJogadorCommand command);
}