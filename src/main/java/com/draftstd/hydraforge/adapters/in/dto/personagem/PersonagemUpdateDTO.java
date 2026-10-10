package com.draftstd.hydraforge.adapters.in.dto.personagem;

public record PersonagemUpdateDTO(
        String historia,
        String aparencia,
        String urlImagem,
        String notasJogador,
        boolean ativo
) {}