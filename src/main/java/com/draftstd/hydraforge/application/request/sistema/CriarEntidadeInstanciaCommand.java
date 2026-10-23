package com.draftstd.hydraforge.application.request.sistema;

import java.util.Map;
import java.util.UUID;

public record CriarEntidadeInstanciaCommand(UUID entidadeSistemaId, UUID donoId, String nome, Map<String, Object> valoresAtributos) {

}