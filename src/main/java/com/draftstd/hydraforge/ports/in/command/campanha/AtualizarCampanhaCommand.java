package com.draftstd.hydraforge.ports.in.command.campanha;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import java.util.UUID;

public record AtualizarCampanhaCommand(UUID campanhaId, String nome, String descricao, StatusCampanha status) {

}