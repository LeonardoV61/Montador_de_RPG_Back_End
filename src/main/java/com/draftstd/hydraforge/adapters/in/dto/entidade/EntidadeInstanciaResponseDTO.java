package com.draftstd.hydraforge.adapters.in.dto.entidade;

import java.time.LocalDateTime;
import tools.jackson.databind.JsonNode;

public record EntidadeInstanciaResponseDTO(
    Long campanhaId,
    Long entidadeSistemaId,
    String tipo,
    String nome,
    String descricao,
    JsonNode atributosAtuais,
    JsonNode customizacoes,
    LocalDateTime criadaEm,
    String urlImagem
) {

}
