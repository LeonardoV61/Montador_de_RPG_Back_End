package com.draftstd.hydraforge.ports.in.command.sessao;

import java.util.UUID;

public record AtualizarAnotacaoCommand(UUID anotacaoId, String titulo, String conteudo, Boolean privada) {

}