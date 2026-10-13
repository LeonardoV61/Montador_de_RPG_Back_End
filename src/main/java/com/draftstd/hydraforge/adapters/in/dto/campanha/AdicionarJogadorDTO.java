package com.draftstd.hydraforge.adapters.in.dto.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AdicionarJogadorDTO(@NotNull(message = "O ID do usuário é obrigatório") UUID usuarioId, @NotNull(message = "O papel é obrigatório") PapeisUsuario papel) {

}