package com.draftstd.hydraforge.adapters.in.dto.campanha;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CampanhaCreateDTO(@NotBlank(message = "O nome da campanha não pode estar vazio") String nome, @NotNull(message = "O ID do sistema é obrigatório") UUID sistemaId, @NotNull(message = "O ID do criador (Mestre) é obrigatório") UUID criadorId, String descricao, String urlImagem) {

}