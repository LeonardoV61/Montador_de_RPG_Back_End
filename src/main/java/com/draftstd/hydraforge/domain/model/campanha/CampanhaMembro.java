package com.draftstd.hydraforge.domain.model.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.enums.StatusMembroCampanha;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record CampanhaMembro(
    UUID usuarioId,
    PapeisUsuario papel,
    StatusMembroCampanha status,
    LocalDateTime entrouEm
) {
    public CampanhaMembro {
        Objects.requireNonNull(usuarioId, "O ID do usuário é obrigatório");
        if (papel == null) {
            papel = PapeisUsuario.JOGADOR;
        }
        if (status == null) {
            status = StatusMembroCampanha.ATIVO;
        }
        if (entrouEm == null) {
            entrouEm = LocalDateTime.now();
        }
    }
}