package com.draftstd.hydraforge.application.request.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.enums.StatusMembroCampanha;
import java.util.UUID;

public record AlterarPapelJogadorCommand(UUID campanhaId, UUID usuarioId, PapeisUsuario novoPapel, StatusMembroCampanha novoStatus) {

}