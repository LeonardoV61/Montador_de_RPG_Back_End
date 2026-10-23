package com.draftstd.hydraforge.adapters.in.dto.sistema.entidade;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record EntidadeInstanciaResponseDTO(UUID id, UUID campanhaId, UUID entidadeSistemaId, String tipo, String nome, String descricao, String urlImagem, Map<String, Object> atributosAtuais, Map<String, Object> customizacoes, LocalDateTime criadaEm) {

}