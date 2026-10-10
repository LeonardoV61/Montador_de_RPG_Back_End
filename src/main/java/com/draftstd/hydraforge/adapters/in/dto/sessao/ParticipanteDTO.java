package com.draftstd.hydraforge.adapters.in.dto.sessao;

import tools.jackson.databind.JsonNode;

public record ParticipanteDTO(
    Long idInstancia,
    String nome,
    String tipo,
    JsonNode posicao,
    JsonNode atributosAtuais
) {}
