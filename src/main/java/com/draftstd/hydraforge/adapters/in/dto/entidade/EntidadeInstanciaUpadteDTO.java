package com.draftstd.hydraforge.adapters.in.dto.entidade;

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
