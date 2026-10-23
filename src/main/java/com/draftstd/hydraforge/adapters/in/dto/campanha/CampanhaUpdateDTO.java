package com.draftstd.hydraforge.adapters.in.dto.campanha;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;

public record CampanhaUpdateDTO(String nome, String descricao, String urlImagem, StatusCampanha status) {

}