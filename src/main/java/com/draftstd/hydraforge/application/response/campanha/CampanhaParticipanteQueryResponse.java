package com.draftstd.hydraforge.application.response.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.enums.StatusMembroCampanha;

import java.time.LocalDateTime;
import java.util.UUID;

public record CampanhaParticipanteQueryResponse(UUID usuarioId, String nomeUsuario, PapeisUsuario papel, StatusMembroCampanha status, LocalDateTime dataEntrada) {

}