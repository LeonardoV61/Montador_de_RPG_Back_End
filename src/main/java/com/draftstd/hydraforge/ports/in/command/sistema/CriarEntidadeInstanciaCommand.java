package com.draftstd.hydraforge.ports.in.command.sistema;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record CriarEntidadeInstanciaCommand(UUID entidadeSistemaId, UUID donoId, String nome, JsonNode valoresAtributosJson) {

}