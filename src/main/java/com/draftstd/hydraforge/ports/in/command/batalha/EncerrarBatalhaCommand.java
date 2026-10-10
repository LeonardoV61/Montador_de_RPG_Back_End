package com.draftstd.hydraforge.ports.in.command.batalha;

import java.util.UUID;

public record EncerrarBatalhaCommand(UUID batalhaId, String motivo) {

}

