package com.draftstd.hydraforge.application.request.batalha;

import java.util.UUID;

public record EncerrarBatalhaCommand(UUID batalhaId, String motivo) {

}

