package com.draftstd.hydraforge.ports.in.command.sistema;

import java.util.UUID;

public record CriarSistemaCommand(String nome, String descricao, String versao, UUID autorId, String regrasBaseJson) {

}