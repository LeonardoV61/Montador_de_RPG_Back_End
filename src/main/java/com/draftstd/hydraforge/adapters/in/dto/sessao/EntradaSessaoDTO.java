package com.draftstd.hydraforge.adapters.in.dto.sessao;

import java.util.UUID;

public record EntradaSessaoDTO(UUID idSessao, UUID idUsuario, boolean isMestre, UUID idInstancia) {

}