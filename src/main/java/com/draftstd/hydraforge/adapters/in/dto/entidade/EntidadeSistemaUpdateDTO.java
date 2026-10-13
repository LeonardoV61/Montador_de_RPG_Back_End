package com.draftstd.hydraforge.adapters.in.dto.entidade;

import tools.jackson.databind.JsonNode;

public record EntidadeSistemaUpdateDTO(String nome, String descricao, String urlImagem, String tipo, JsonNode atributos, JsonNode propriedades) {
    public JsonNode schemaJson() {
        return atributos;
    }
}