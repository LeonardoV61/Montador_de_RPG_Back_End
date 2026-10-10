package com.draftstd.hydraforge.ports.in.command.mecanica;

import java.util.Map;
import java.util.UUID;

public record RolarDadosCommand(UUID sessaoId, UUID jogadorId, UUID personagemId, String expressaoDados, String motivo, Map<String, Object> contextoAdicional) {

}