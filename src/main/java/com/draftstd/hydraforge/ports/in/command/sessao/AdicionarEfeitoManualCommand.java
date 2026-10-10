package com.draftstd.hydraforge.ports.in.command.sessao;

import java.util.UUID;

public record AdicionarEfeitoManualCommand(UUID sessaoId, UUID alvoId, String nome, String descricao, Integer duracaoTurnos, String modificadoresJson) {

}