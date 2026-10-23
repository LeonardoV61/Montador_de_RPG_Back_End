package com.draftstd.hydraforge.application.request.importacao;

import java.util.UUID;

public record ImportarPersonagemCommand(UUID usuarioId, UUID campanhaId, byte[] conteudoArquivo, String formato) {

}