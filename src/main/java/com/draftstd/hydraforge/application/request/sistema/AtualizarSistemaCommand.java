package com.draftstd.hydraforge.application.request.sistema;

import java.util.Map;
import java.util.UUID;

public record AtualizarSistemaCommand(UUID sistemaId, String nome, String descricao, String urlImagem, Map<String, Object> configuracao, Map<String, Object> schemaAtributos, Map<String, Object> schemaEntidades, Map<String, Object> schemaResolucoes) {

}