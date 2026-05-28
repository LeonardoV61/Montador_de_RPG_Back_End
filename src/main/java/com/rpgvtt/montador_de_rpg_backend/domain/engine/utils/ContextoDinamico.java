package com.rpgvtt.montador_de_rpg_backend.domain.engine.utils;

import com.rpgvtt.montador_de_rpg_backend.domain.engine.primitivos.EstadoSessao;
import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeInstancia;
import com.rpgvtt.montador_de_rpg_backend.domain.validation.schema.AtributoSchema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;
import java.util.function.Function;

public class ContextoDinamico implements Contexto {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final EstadoSessao estadoSessao;
    private final Long idEntidade;
    private final Map<String, AtributoSchema> schemasAtributos;
    private final Function<JsonNode, ResultadoExpressao> interpretador;
    private final Set<String> caminhosEmAvaliacao = new HashSet<>(); 

    public ContextoDinamico(EstadoSessao estadoSessao,
                            Long idEntidade,
                            Map<String, AtributoSchema> schemasAtributos,
                            Function<JsonNode, ResultadoExpressao> interpretador) {

        this.estadoSessao = estadoSessao;
        this.idEntidade = idEntidade;
        this.schemasAtributos = schemasAtributos;
        this.interpretador = interpretador;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Object> get(String caminho) {
        
        if (caminhosEmAvaliacao.contains(caminho)) {
            throw new IllegalStateException("Loop detectado ao avaliar caminho: " + caminho);
        }

        caminhosEmAvaliacao.add(caminho);

        try {
            EntidadeInstancia entidade = null;
            if (idEntidade != null) {
                entidade = estadoSessao.getEntidades().get(idEntidade);
                if (entidade == null) return Optional.empty();
            }

            Object valorBase = null;
            if (entidade != null) {
                JsonNode raiz = entidade.getAtributosAtuais();
                String[] partes = caminho.split("\\.");
                JsonNode atual = raiz;
                for (String parte : partes) {
                    if (atual == null || !atual.isObject()) break;
                    atual = atual.get(parte);
                }
                if (atual != null && !atual.isMissingNode()) {
                    valorBase = converterJsonNode(atual);
                }
            }

            String atributoRaiz = caminho.split("\\.")[0];
            AtributoSchema schema = schemasAtributos.get(atributoRaiz);
            if (schema != null && schema.isDerivado() && schema.getFormula() != null) {
                JsonNode formulaNode = objectMapper.readTree(schema.getFormula());
                ResultadoExpressao resultado = interpretador.apply(formulaNode);
                if (caminho.equals(atributoRaiz)) {
                    valorBase = resultado.getValor();
                } else {
                    if (resultado.getValor() instanceof Map) {
                        valorBase = navegarMapa((Map<String, Object>) resultado.getValor(),
                                caminho.substring(atributoRaiz.length() + 1));
                    } else {
                        throw new IllegalArgumentException("Fórmula do atributo composto derivado '" + atributoRaiz + "' não retornou um objeto.");
                    }
                }
            }

            return Optional.ofNullable(valorBase);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao avaliar caminho: " + caminho, e);
        } finally {
            caminhosEmAvaliacao.remove(caminho);
        }
    }

    
    private Object converterJsonNode(JsonNode node) {
        if (node.isNumber()) return node.numberValue();
        if (node.isTextual()) return node.asText();
        if (node.isBoolean()) return node.booleanValue();
        if (node.isArray()) {
            List<Object> lista = new ArrayList<>();
            for (JsonNode item : node) {
                lista.add(converterJsonNode(item));
            }
            return lista;
        }
        if (node.isObject()) {
            Map<String, Object> mapa = new LinkedHashMap<>();
            Iterator<String> it = node.fieldNames();
            while (it.hasNext()) {
                String key = it.next();
                mapa.put(key, converterJsonNode(node.get(key)));
            }
            return mapa;
        }
        return null;
    }
    
    private Object navegarMapa(Map<String, Object> mapa, String caminhoRestante) {
        String[] partes = caminhoRestante.split("\\.");
        Object atual = mapa;
        for (String parte : partes) {
            if (atual instanceof Map) {
                atual = ((Map<?, ?>) atual).get(parte);
            } else {
                return null;
            }
        }
        return atual;
    }


}