package com.draftstd.hydraforge.application.request.sessao;

import java.util.UUID;

public record CriarAnotacaoCommand(UUID sessaoId, UUID usuarioId, UUID campanhaId, String titulo, String conteudo, String categoria, String anotacaoComplexaJson, Boolean ePrivado) {

}