package com.draftstd.hydraforge.adapters.in.dto.sistema;

import java.util.Map;

public record ProcedimentoUpdateDTO(String nome, String descricao, String tipo, Map<String, Object> configsGeral) {

}