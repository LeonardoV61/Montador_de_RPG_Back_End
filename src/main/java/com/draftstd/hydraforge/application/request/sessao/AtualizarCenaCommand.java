package com.draftstd.hydraforge.application.request.sessao;

import java.util.UUID;

public record AtualizarCenaCommand(UUID cenaId, String mapaJson, String urlMapa, Integer ordem, String tipo, String estadoJson) {

}