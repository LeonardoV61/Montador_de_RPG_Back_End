package com.draftstd.hydraforge.adapters.in.websocket;

import com.draftstd.hydraforge.ports.in.batalha.GerenciarParticipantesBatalhaUseCase;
import com.draftstd.hydraforge.application.request.batalha.AdicionarParticipanteBatalhaCommand;
import com.draftstd.hydraforge.application.response.batalha.BatalhaQueryResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class BattleController {

    private final GerenciarParticipantesBatalhaUseCase gerenciarParticipantesBatalhaUseCase;

    public BattleController(GerenciarParticipantesBatalhaUseCase gerenciarParticipantesBatalhaUseCase) {
        this.gerenciarParticipantesBatalhaUseCase = gerenciarParticipantesBatalhaUseCase;
    }

    @MessageMapping("/batalha/{batalhaId}/participante/adicionar")
    @SendTo("/topic/batalha/{batalhaId}")
    public BatalhaQueryResponse adicionarParticipanteWs(@DestinationVariable UUID batalhaId,
                                                        AdicionarParticipanteBatalhaCommand command) {
        // Assegura que o ID da rota é o mesmo do comando
        AdicionarParticipanteBatalhaCommand cmdSanitizado = new AdicionarParticipanteBatalhaCommand(
                batalhaId,
                command.entidadeInstanciaId(),
                command.tipo(),
                command.iniciativa()
        );
        return gerenciarParticipantesBatalhaUseCase.adicionarParticipante(cmdSanitizado);
    }
}