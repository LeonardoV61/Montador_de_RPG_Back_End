package com.draftstd.hydraforge.ports.in.command.sistema;

import java.util.UUID;

public record AtualizarSistemaCommand(UUID sistemaId, String nome, String descricao, String versao, String regrasBaseJson) {

}