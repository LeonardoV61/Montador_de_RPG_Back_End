package com.rpgvtt.montador_de_rpg_backend.dto.personagem;

import java.time.LocalDateTime;

public record PersonagemResponseDTO(
        Long id,
        Long usuarioId,
        String usuarioEmail,
        Long campanhaId,
        String campanhaNome,
        Long instanciaId,
        String instanciaNome,
        String historia,
        String aparencia,
        String urlImagem,
        String notasJogador,
        boolean ativo,
        LocalDateTime criadoEm
) {}