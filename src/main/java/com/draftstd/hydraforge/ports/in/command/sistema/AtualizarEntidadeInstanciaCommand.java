package com.draftstd.hydraforge.ports.in.command.sistema;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record AtualizarEntidadeInstanciaCommand(UUID instanciaId, String nome, JsonNode valoresAtributosJson ) {

}