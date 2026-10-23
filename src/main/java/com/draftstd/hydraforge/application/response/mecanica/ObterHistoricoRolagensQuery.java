package com.draftstd.hydraforge.application.response.mecanica;

import java.util.UUID;

public record ObterHistoricoRolagensQuery(UUID sessaoId, UUID personagemId, Integer limite) {

}