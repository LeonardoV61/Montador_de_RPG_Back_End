package com.draftstd.hydraforge.ports.in.command.sessao;

import java.util.UUID;

public record AtualizarCenaCommand(UUID cenaId, String titulo, String ambiente, String mapaUrl, Boolean ativa) {

}