package com.draftstd.hydraforge.adapters.in.dto.entidade;

import java.time.LocalDateTime;
import java.util.UUID;
import tools.jackson.databind.JsonNode;

public record EntidadeInstanciaResponseDTO(UUID campanhaId, UUID entidadeSistemaId, String tipo, String nome, String descricao, JsonNode atributosAtuais, JsonNode customizacoes, LocalDateTime criadaEm, String urlImagem) {

}
