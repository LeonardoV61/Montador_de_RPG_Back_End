package com.draftstd.hydraforge.ports.in.command.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import java.util.UUID;

public record AlterarPapelJogadorCommand(UUID campanhaId, UUID usuarioId, PapeisUsuario novoPapel) {

}