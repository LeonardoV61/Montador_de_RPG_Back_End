package com.draftstd.hydraforge.adapters.in.dto.sistema;

import java.util.Map;

public record EventoSistemaUpdateDTO(String nome, String descricao, Map<String, Object> payloadSchema) {

}