package com.draftstd.hydraforge.application.request.sistema;

import java.util.Map;
import java.util.UUID;

public record CriarSistemaCommand(UUID usuarioCriadorId, UUID sistemaPaiId, String nome, String descricao, String urlImagem, Map<String, Object> configuracao, Map<String, Object> schemaAtributos, Map<String, Object> schemaEntidades, Map<String, Object> schemaResolucoes, boolean eOficial) {

}