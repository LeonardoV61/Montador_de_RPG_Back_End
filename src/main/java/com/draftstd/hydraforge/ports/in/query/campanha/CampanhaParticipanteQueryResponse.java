package com.draftstd.hydraforge.ports.in.query.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import java.time.LocalDateTime;
import java.util.UUID;

public record CampanhaParticipanteQueryResponse(UUID usuarioId, String nomeUsuario, PapeisUsuario papel, LocalDateTime dataEntrada) {

}
