package com.draftstd.hydraforge.adapters.in.websocket;

import com.draftstd.hydraforge.ports.in.command.sessao.CriarAnotacaoCommand;
import com.draftstd.hydraforge.ports.in.sessao.CriarAnotacaoUseCase;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class SessaoWebSocketController {

    private final CriarAnotacaoUseCase criarAnotacaoUseCase;

    public SessaoWebSocketController(CriarAnotacaoUseCase criarAnotacaoUseCase) {
        this.criarAnotacaoUseCase = criarAnotacaoUseCase;
    }

    @MessageMapping("/sessao.anotar")
    public void processarAnotacao(@Payload CriarAnotacaoCommand command) {
        criarAnotacaoUseCase.executar(command);
    }
}