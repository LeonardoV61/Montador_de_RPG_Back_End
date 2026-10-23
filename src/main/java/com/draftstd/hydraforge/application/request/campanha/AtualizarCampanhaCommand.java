package com.draftstd.hydraforge.application.request.campanha;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import java.util.UUID;

public record AtualizarCampanhaCommand(UUID campanhaId, String nome, String descricao, String urlImagem, StatusCampanha status) {

}