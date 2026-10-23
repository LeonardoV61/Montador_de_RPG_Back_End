package com.draftstd.hydraforge.application.request.importacao;

import java.util.UUID;

public record ImportarSistemaCommand(UUID autorId, byte[] conteudoArquivo, String formato) {

}