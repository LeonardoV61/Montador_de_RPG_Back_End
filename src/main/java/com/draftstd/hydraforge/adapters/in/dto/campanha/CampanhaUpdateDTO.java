package com.draftstd.hydraforge.adapters.in.dto.campanha;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import java.util.UUID;

public record CampanhaUpdateDTO(
    String nome,
    String descricao,
    String urlImagem,
    StatusCampanha status,
    UUID sistemaId        
) {}