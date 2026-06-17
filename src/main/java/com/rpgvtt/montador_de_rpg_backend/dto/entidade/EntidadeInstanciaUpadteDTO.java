package com.rpgvtt.montador_de_rpg_backend.dto.entidade;

import tools.jackson.databind.JsonNode;

public record EntidadeInstanciaUpadteDTO(
    String nome,
    String descricao,
    String tipo,
    JsonNode atributosAtuais,
    JsonNode customizacoes,
    String urlImagem

) {

}
