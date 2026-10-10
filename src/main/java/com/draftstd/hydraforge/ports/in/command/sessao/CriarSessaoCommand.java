package com.draftstd.hydraforge.ports.in.command.sessao;

import java.time.LocalDateTime;
import java.util.UUID;

public record CriarSessaoCommand(UUID campanhaId, String titulo, String descricao, LocalDateTime agendadaPara) {

}