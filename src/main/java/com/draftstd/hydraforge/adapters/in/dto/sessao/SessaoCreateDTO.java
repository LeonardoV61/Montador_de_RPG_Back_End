package com.draftstd.hydraforge.adapters.in.dto.sessao;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record SessaoCreateDTO(@NotNull(message = "O ID da campanha é obrigatório para iniciar uma sessão.") UUID campanhaId, @FutureOrPresent(message = "A data de início deve estar no presente ou futuro.") LocalDateTime dataInicio) {

}