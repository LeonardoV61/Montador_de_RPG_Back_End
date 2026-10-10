package com.draftstd.hydraforge.adapters.in.dto.batalha;

import com.draftstd.hydraforge.domain.enums.BatalhaStatus;

public record BatalhaDTO(Long idBatalha, BatalhaStatus status, int rodadaAtual) {
}
