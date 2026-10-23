package com.draftstd.hydraforge.adapters.in.dto.personagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

public record PersonagemCreateDTO(@NotNull(message = "O ID do sistema é obrigatório") UUID sistemaId, UUID campanhaId, UUID instanciaId, @NotBlank(message = "O nome é obrigatório") String nome, String historia, String aparencia, String urlImagem, String notasJogador, Map<String, Object> atributos) {

}