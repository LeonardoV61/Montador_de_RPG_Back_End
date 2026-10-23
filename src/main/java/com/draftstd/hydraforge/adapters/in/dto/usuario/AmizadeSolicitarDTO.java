package com.draftstd.hydraforge.adapters.in.dto.usuario;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AmizadeSolicitarDTO(@NotNull(message = "O ID do destinatário é obrigatório") UUID destinatarioId) {

}