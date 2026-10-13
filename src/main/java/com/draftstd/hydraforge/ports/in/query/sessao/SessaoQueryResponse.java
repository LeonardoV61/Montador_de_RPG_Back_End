package com.draftstd.hydraforge.ports.in.query.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import java.util.List;
import java.util.UUID;

public record SessaoQueryResponse(UUID id, UUID campanhaId, StatusSessao status, List<MensagemLogQueryResponse> mensagens) {
    
}