package com.draftstd.hydraforge.ports.in.command.sistema;

import java.util.UUID;

public record AtualizarEntidadeInstanciaCommand(UUID instanciaId, String nome, String valoresAtributosJson) {

}
