package com.draftstd.hydraforge.application.request.campanha;

import java.util.UUID;

public record CriarCampanhaCommand(String nome, String descricao, String urlImagem, UUID sistemaId, UUID mestreId) {

}