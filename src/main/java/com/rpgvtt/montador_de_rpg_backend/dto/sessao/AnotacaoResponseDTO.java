package com.rpgvtt.montador_de_rpg_backend.dto.sessao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.JsonNode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnotacaoResponseDTO {
    private Integer id;
    private Long usuarioId;
    private Long campanhaId;
    private Long sessaoId;
    private String titulo;
    private String conteudo;
    private String categoria;
    private JsonNode anotacaoComplexa;
    private Boolean ePrivado;
}
