package com.draftstd.hydraforge.adapters.in.dto.sistema.entidade;

import java.util.Map;

public record EntidadeInstanciaUpdateDTO(String nome, String descricao, String tipo, String urlImagem, Map<String, Object> atributosAtuais, Map<String, Object> customizacoes) {
    public Map<String, Object> valoresAtributos() {
        return atributosAtuais;
    }
}