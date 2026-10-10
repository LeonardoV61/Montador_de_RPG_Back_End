package com.draftstd.hydraforge.adapters.in.dto.campanha;

import jakarta.validation.constraints.NotNull;

public record AdicionarJogadorDTO(@NotNull(message = "O ID do usuário é obrigatório") Long usuarioId) {

}
