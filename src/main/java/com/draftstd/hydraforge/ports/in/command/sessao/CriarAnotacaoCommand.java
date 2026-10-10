package com.draftstd.hydraforge.ports.in.command.sessao;

import java.util.UUID;

public record CriarAnotacaoCommand(UUID sessaoId, UUID autorId, String titulo, String conteudo, Boolean privada) {

}