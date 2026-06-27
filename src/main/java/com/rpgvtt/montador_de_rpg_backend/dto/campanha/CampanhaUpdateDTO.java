package com.rpgvtt.montador_de_rpg_backend.dto.campanha;

public record CampanhaUpdateDTO(
    String nome,
    String descricao,
    String urlImagem,
    String status,
    Long sistemaId
) {}