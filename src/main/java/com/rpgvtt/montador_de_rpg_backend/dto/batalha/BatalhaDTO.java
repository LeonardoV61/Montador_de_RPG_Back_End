package com.rpgvtt.montador_de_rpg_backend.dto.batalha;

import com.rpgvtt.montador_de_rpg_backend.domain.enums.BatalhaStatus;

public record BatalhaDTO(Long idBatalha, BatalhaStatus status, int rodadaAtual) {
}
