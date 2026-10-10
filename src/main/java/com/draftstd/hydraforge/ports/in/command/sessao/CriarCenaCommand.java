package com.draftstd.hydraforge.ports.in.command.sessao;

import java.util.UUID;

public record CriarCenaCommand(UUID sessaoId, String titulo, String ambiente, String mapaUrl) {

}