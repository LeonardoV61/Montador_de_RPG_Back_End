package com.draftstd.hydraforge.ports.in.campanha;

import com.draftstd.hydraforge.application.request.campanha.AdicionarJogadorCampanhaCommand;
import com.draftstd.hydraforge.application.request.campanha.AlterarPapelJogadorCommand;
import com.draftstd.hydraforge.application.request.campanha.RemoverJogadorCampanhaCommand;

public interface GerenciarParticipantesCampanhaUseCase {
    void adicionarJogador(AdicionarJogadorCampanhaCommand command);
    void removerJogador(RemoverJogadorCampanhaCommand command);
    void alterarPapel(AlterarPapelJogadorCommand command);
}