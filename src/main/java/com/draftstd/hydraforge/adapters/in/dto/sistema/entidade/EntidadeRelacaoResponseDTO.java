package com.draftstd.hydraforge.adapters.in.dto.sistema.entidade;

import java.util.Map;
import java.util.UUID;

public record EntidadeRelacaoResponseDTO(UUID id, UUID idEntidadePai, String nomeEntidadePai, UUID idEntidadeFilha, String nomeEntidadeFilha, Integer quantidade, Map<String, Object> customizacoes, String origem) {

}