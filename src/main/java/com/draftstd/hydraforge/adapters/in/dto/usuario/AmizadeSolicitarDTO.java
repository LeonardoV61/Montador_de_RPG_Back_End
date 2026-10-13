package com.draftstd.hydraforge.adapters.in.dto.usuario;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record AmizadeSolicitarDTO(@NotNull(message = "O ID do remetente é obrigatório") UUID remetenteId, @NotNull(message = "O ID do destinatário é obrigatório") UUID destinatarioId) {

}