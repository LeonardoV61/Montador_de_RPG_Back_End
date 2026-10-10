package com.draftstd.hydraforge.ports.in.query.mecanica;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ResultadoRolagemQueryResponse(UUID id, UUID sessaoId, UUID jogadorId, UUID personagemId, String expressaoOriginal, List<Integer> dadosSorteados, Integer modificadorTotal, Integer resultadoFinal, String motivo, LocalDateTime timestamp) {

}