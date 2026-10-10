package com.draftstd.hydraforge.ports.in.command.importacao;

import java.util.UUID;

public record ImportarPersonagemCommand(UUID usuarioId, UUID campanhaId, byte[] conteudoArquivo, String formato) {

}