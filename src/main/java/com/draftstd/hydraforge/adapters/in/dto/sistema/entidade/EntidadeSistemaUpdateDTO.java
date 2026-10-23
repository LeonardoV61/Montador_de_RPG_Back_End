package com.draftstd.hydraforge.adapters.in.dto.sistema.entidade;

import java.util.Map;

public record EntidadeSistemaUpdateDTO(String nome, String descricao, String urlImagem, String tipo, Map<String, Object> atributos, Map<String, Object> propriedades) {
    public Map<String, Object> schema() {
        return atributos;
    }
}