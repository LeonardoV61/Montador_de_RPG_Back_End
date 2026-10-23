package com.draftstd.hydraforge.application.request.sessao;

import java.time.LocalDateTime;
import java.util.UUID;

public record CriarSessaoCommand(UUID campanhaId, String titulo, int ordem, LocalDateTime dataInicio) {

}