package com.rpgvtt.montador_de_rpg_backend.engine.procedimentos;

import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EtapaExecutavel;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

public record EtapaAvulsa(String nome, String tipoEtapa, JsonNode parametrosEtapa) implements EtapaExecutavel {

    @Override public String getNome() { return nome; }
    @Override public String getTipoEtapa() { return tipoEtapa; }
    @Override public JsonNode getParametrosEtapa() { return parametrosEtapa; }

    public static EtapaAvulsa de(String tipoEtapa, JsonNode parametros) {
        return new EtapaAvulsa("Execução avulsa: " + tipoEtapa, tipoEtapa, parametros);
    }

    public static EtapaAvulsa de(String tipoEtapa, Map<String, Object> parametros, JsonMapper mapper) {
        return de(tipoEtapa, mapper.valueToTree(parametros));
    }
}
