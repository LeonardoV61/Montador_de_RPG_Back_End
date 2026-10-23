package com.draftstd.hydraforge.adapters.in.dto.sistema.entidade;

import java.util.Map;

public record EntidadeRelacaoUpdateDTO(Integer quantidade, Map<String, Object> customizacoes, String origem) {

}