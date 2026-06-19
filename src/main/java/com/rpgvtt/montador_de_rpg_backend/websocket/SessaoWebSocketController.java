package com.rpgvtt.montador_de_rpg_backend.websocket;

import com.rpgvtt.montador_de_rpg_backend.dto.sistema.ProcedimentoContextoDTO;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.ProcedimentoEngine;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ProcedimentoContexto;
import com.rpgvtt.montador_de_rpg_backend.service.sessao.SessaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Map;

import static com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ProcedimentoContexto.Status.AGUARDANDO_INPUT_MULTIPLO;

@Controller
@RequiredArgsConstructor
public class SessaoWebSocketController {

    private final ProcedimentoEngine engine;
    private final SessaoService sessaoService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Player sends an action or responds to an input request.
     *
     * Message payload: { "acao_escolhida": "ATACAR", "id_alvo": 42 }
     * or any Map<String, Object> matching what the pending SOLICITAR_INPUT expects.
     */
    @MessageMapping("/sessao/{idSessao}/acao")
    public void receberAcao(@DestinationVariable Long idSessao,
                            @Payload Map<String, Object> input,
                            Principal principal) {

        Long idUsuario = Long.parseLong(principal.getName());
        Long idPersonagem = sessaoService.resolverInstanciaDoJogador(idSessao, idUsuario);

        ProcedimentoContexto ctx = engine.responder(idSessao, input);
        ProcedimentoContextoDTO resposta = ProcedimentoContextoDTO.from(ctx);

        messagingTemplate.convertAndSend(
                "/topic/sessao/" + idSessao, resposta);
    }

    /**
     * Player declares action during a parallel-input phase (Mythic Bastionland etc.)
     * Broadcasts partial status to declarer, full result only when all replied.
     */
    @MessageMapping("/sessao/{idSessao}/declarar")
    public void receberDeclaracao(@DestinationVariable Long idSessao,
                                  @Payload Map<String, Object> input,
                                  Principal principal) {

        Long idUsuario    = Long.parseLong(principal.getName());
        Long idPersonagem = sessaoService.resolverInstanciaDoJogador(idSessao, idUsuario);

        ProcedimentoContexto ctx = engine.responder(idSessao, input);
        ProcedimentoContextoDTO resposta = ProcedimentoContextoDTO.from(ctx);

        if (resposta.getStatus() == AGUARDANDO_INPUT_MULTIPLO) {
            // Acknowledge only to the player who just declared
            messagingTemplate.convertAndSendToUser(
                    principal.getName(),
                    "/queue/sessao/" + idSessao,
                    Map.of("tipo", "DECLARACAO_RECEBIDA",
                            "pendentes", resposta.getInputSolicitado())
            );
            // Broadcast pending list to room so UI shows waiting indicator
            messagingTemplate.convertAndSend(
                    "/topic/sessao/" + idSessao + "/status",
                    (Object) Map.of("tipo", "AGUARDANDO_DECLARACOES",
                            "pendentes", resposta.getInputSolicitado())
            );
        } else {
            // All players declared — broadcast full result
            messagingTemplate.convertAndSend(
                    "/topic/sessao/" + idSessao, resposta);
        }
    }

    /**
     * Player disconnects from WebSocket — remove from room roster.
     */
    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String name = accessor.getUser() != null ? accessor.getUser().getName() : null;
        if (name == null) return;

        Long idUsuario = Long.parseLong(name);

        // Find which sessions this user was in and remove them
        // (SessaoParticipanteCache handles multi-session users correctly)
        sessaoService.sairDaSessao(null, idUsuario); // overload with no idSessao scans all
    }
}