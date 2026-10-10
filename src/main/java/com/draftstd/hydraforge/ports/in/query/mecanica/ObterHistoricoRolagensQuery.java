package com.draftstd.hydraforge.ports.in.query.mecanica;

import java.util.UUID;

public record ObterHistoricoRolagensQuery(UUID sessaoId, UUID personagemId, Integer limite) {

}