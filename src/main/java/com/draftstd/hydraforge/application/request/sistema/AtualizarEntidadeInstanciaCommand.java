package com.draftstd.hydraforge.application.request.sistema;

import java.util.Map;
import java.util.UUID;

public record AtualizarEntidadeInstanciaCommand(UUID instanciaId, String nome, Map<String, Object> valoresAtributos) {

}