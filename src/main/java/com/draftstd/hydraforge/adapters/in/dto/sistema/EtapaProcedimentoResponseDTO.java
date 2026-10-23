package com.draftstd.hydraforge.adapters.in.dto.sistema;

import java.util.Map;
import java.util.UUID;

public record EtapaProcedimentoResponseDTO(UUID id, UUID procedimentoId, Integer ordem, String nome, String tipoEtapa, Map<String, Object> parametrosEtapa, Boolean obrigatorio) {

}