package com.draftstd.hydraforge.adapters.in.dto.campanha;

public record CampanhaUpdateDTO(
    String nome,
    String descricao,
    String urlImagem,
    String status,
    Long sistemaId
) {}