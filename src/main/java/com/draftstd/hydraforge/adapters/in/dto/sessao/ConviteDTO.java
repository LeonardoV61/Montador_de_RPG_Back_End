package com.draftstd.hydraforge.adapters.in.dto.sessao;

import java.util.UUID;

public record ConviteDTO(UUID idSessao, UUID idUsuario, String token) {

}