package com.draftstd.hydraforge.application.request.sessao;

import java.util.UUID;

public record AtualizarAnotacaoCommand(UUID anotacaoId, String titulo, String conteudo, String categoria, Boolean ePrivado) {

}