package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record EntidadeProcedimentoCreateDTO(@NotNull(message = "O ID da entidade é obrigatório") UUID entidadeId, @NotNull(message = "O ID do procedimento é obrigatório") UUID procedimentoId, String processamento, @NotNull(message = "A condição é obrigatória") JsonNode condicao, boolean eReativo, Integer ordem) {

}