package com.draftstd.hydraforge.adapters.in.dto.personagem;

import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import java.util.List;
import java.util.UUID;

public record UsoItemDTO(UUID itemId, UUID alvoId, String nomeItem, boolean restamUnidades, List<ResultadoEtapa> resultados) {

}