package com.draftstd.hydraforge.adapters.in.dto.personagem;

import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;

import java.util.List;

public record UsoItemDTO(
        String   idItem,
        String   nomeItem,
        Long     idInstanciaAlvo,
        boolean  restamUnidades,
        List<ResultadoEtapa> resultados
) {}