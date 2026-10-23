package com.draftstd.hydraforge.application.request.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.enums.StatusMembroCampanha;
import java.util.UUID;

public record AdicionarJogadorCampanhaCommand(UUID campanhaId, UUID usuarioId, PapeisUsuario papel, StatusMembroCampanha statusMembro) {

}