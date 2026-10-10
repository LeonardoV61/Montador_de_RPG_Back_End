package com.draftstd.hydraforge.adapters.in.dto.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;

import java.time.LocalDateTime;

public record SessaoDTO(Long idSessao, StatusSessao status, LocalDateTime dataInicio, Long idCampanha) {
}
