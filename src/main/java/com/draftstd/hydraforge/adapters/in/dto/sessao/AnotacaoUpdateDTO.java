package com.draftstd.hydraforge.adapters.in.dto.sessao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.JsonNode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnotacaoUpdateDTO {
    private String titulo;
    private String conteudo;
    private String categoria;
    private JsonNode anotacaoComplexa;
    private Boolean ePrivado;
}
