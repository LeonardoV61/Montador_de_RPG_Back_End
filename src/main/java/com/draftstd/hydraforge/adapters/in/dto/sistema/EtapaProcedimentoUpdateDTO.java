package com.draftstd.hydraforge.adapters.in.dto.sistema;

import java.util.Map;

public record EtapaProcedimentoUpdateDTO(Integer ordem, String nome, String tipoEtapa, Map<String, Object> parametrosEtapa, Boolean obrigatorio) {

}