package com.draftstd.hydraforge.adapters.in.dto.sistema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public record SistemaCreateDTO(@NotNull(message = "O ID do criador é obrigatório") UUID criadorId, UUID sistemaPaiId, @NotBlank(message = "O nome é obrigatório") String nome, String descricao, String urlImagem, Map<String, Object> configuracao, Map<String, Object> schemaAtributos, Map<String, Object> schemaEntidades, Map<String, Object> schemaResolucoes, boolean eOficial) {

}