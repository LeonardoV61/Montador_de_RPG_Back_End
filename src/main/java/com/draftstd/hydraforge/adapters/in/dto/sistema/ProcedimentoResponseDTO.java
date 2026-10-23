package com.draftstd.hydraforge.adapters.in.dto.sistema;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ProcedimentoResponseDTO(UUID id, UUID sistemaId, String sistemaNome, String nome, String descricao, String tipo, Map<String, Object> configsGeral, List<EtapaProcedimentoResponseDTO> etapas) {

}