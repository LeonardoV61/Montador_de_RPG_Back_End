package com.draftstd.hydraforge.ports.in.batalha;

import com.draftstd.hydraforge.ports.in.command.batalha.AdicionarParticipanteBatalhaCommand;
import com.draftstd.hydraforge.ports.in.command.batalha.RemoverParticipanteBatalhaCommand;
import com.draftstd.hydraforge.ports.in.query.batalha.BatalhaQueryResponse;

public interface GerenciarParticipantesBatalhaUseCase {
    BatalhaQueryResponse adicionarParticipante(AdicionarParticipanteBatalhaCommand command);
    BatalhaQueryResponse removerParticipante(RemoverParticipanteBatalhaCommand command);
}