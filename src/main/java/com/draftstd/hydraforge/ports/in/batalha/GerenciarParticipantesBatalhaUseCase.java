package com.draftstd.hydraforge.ports.in.batalha;

import com.draftstd.hydraforge.application.request.batalha.AdicionarParticipanteBatalhaCommand;
import com.draftstd.hydraforge.application.request.batalha.RemoverParticipanteBatalhaCommand;
import com.draftstd.hydraforge.application.response.batalha.BatalhaQueryResponse;

public interface GerenciarParticipantesBatalhaUseCase {
    BatalhaQueryResponse adicionarParticipante(AdicionarParticipanteBatalhaCommand command);
    BatalhaQueryResponse removerParticipante(RemoverParticipanteBatalhaCommand command);
}