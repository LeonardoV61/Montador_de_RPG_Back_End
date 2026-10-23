package com.draftstd.hydraforge.adapters.in.dto.sessao;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record ParticipanteDTO(UUID idInstancia, String nome, String tipo, JsonNode posicao, JsonNode atributosAtuais) {

}