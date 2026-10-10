package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import java.util.List;

public record TesteLivreDTO(
        String atributo,
        String dado,
        List<Integer> dados,
        int modificador,
        int total,
        Integer dificuldade,
        Boolean sucesso
) { }
