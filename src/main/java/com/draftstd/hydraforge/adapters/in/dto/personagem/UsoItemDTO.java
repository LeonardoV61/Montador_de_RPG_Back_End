package com.draftstd.hydraforge.adapters.in.dto.personagem;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UsoItemDTO(@NotNull(message = "O ID do item é obrigatório") UUID itemId, UUID alvoId, UUID sessaoId) {

}