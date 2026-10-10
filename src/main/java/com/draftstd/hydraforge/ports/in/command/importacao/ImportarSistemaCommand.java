package com.draftstd.hydraforge.ports.in.command.importacao;

import java.util.UUID;

public record ImportarSistemaCommand(UUID autorId, byte[] conteudoArquivo, String formato) {

}