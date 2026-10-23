package com.draftstd.hydraforge.application.request.sessao;

import java.util.UUID;

public record CriarMensagemLogCommand(UUID sessaoId, UUID emissorId, String conteudo) {

}