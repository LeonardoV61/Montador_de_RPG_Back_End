package com.draftstd.hydraforge.adapters.in.dto.personagem;

import java.util.Map;
import java.util.UUID;

public record PersonagemUpdateDTO(String nome, String historia, String aparencia, String urlImagem, String notasJogador, Map<String, Object> atributos, UUID sessaoId) {

}