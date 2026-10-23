package com.draftstd.hydraforge.adapters.in.dto.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import java.time.LocalDateTime;
import java.util.UUID;

public record SessaoDTO(UUID idSessao, StatusSessao status, LocalDateTime dataInicio, UUID idCampanha) {

}