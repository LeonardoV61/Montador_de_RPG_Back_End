package com.draftstd.hydraforge.application.request.personagem;

import java.util.Map;
import java.util.UUID;

public record CriarPersonagemCommand(UUID usuarioId, UUID sistemaId, UUID campanhaId, UUID instanciaId, String nome, String historia, String aparencia, String urlImagem, String notasJogador, Map<String, Object> atributos) {

}