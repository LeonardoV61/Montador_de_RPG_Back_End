package com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonMapperUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(JsonNode node) {
        if (node == null || node.isNull()) return Map.of();
        return OBJECT_MAPPER.convertValue(node, Map.class);
    }

    public static JsonNode toJsonNode(Map<String, Object> map) {
        if (map == null || map.isEmpty()) return null;
        return OBJECT_MAPPER.valueToTree(map);
    }
}