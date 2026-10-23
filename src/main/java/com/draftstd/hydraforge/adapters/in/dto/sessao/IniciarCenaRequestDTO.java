package com.draftstd.hydraforge.adapters.in.dto.sessao;

import java.util.List;
import java.util.UUID;

public record IniciarCenaRequestDTO(UUID idSessao, List<UUID> idsInstanciasJogadores, List<UUID> idsInstanciasInimigos, List<List<UUID>> lados, String tipo, Integer ordem) {

}