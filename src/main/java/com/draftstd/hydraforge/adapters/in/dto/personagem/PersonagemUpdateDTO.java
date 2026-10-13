package com.draftstd.hydraforge.adapters.in.dto.personagem;

import tools.jackson.databind.JsonNode;

public record PersonagemUpdateDTO(String nome, String conceito, JsonNode dadosJson, String historia, String aparencia, String urlImagem, String notasJogador, boolean ativo) {

}