package com.draftstd.hydraforge.adapters.in.dto.personagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record PersonagemCompletoCreateDTO(@NotNull(message = "O ID do usuário é obrigatório") UUID usuarioId, UUID campanhaId, @NotNull(message = "O ID da entidade sistema é obrigatório") UUID entidadeSistemaId, @NotBlank(message = "O tipo é obrigatório") String tipo, @NotBlank(message = "O nome é obrigatório") String nome, String descricao, @NotNull(message = "Os atributos são obrigatórios") JsonNode atributosAtuais, JsonNode customizacoes, String urlImagem, String historia, String aparencia, String notasJogador) {

}