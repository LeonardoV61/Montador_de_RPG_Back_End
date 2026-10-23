// domain/validation/SchemaValidator.java
package com.draftstd.hydraforge.domain.validation;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.domain.validation.exception.*;
import com.draftstd.hydraforge.domain.validation.schema.*;

import java.util.List;
import java.util.Map;

public class SchemaValidator {

    // valida a consistência interna dos schemas do sistema
    public void validarConsistenciaSchemas(Sistema sistema, Map<String, AtributoSchema> schemaAtributos, Map<String, TipoEntidadeSchema> schemaEntidades) {

        for (Map.Entry<String, TipoEntidadeSchema> entry : schemaEntidades.entrySet()) {
            String nomesTipo = entry.getKey();
            TipoEntidadeSchema tipoSchema = entry.getValue();

            // Verifica se cada atributo que o tipo usa existe em schema_atributos
            if (tipoSchema.getAtributos() != null) {
                for (String nomeAtributo : tipoSchema.getAtributos()) {
                    if (!schemaAtributos.containsKey(nomeAtributo)) {
                        throw new SchemaInconsistenteException(nomesTipo, nomeAtributo);
                    }
                }
            }

            if (tipoSchema.getObrigatorios() != null) {
                for (String obrigatorio : tipoSchema.getObrigatorios()) {
                    if (!tipoSchema.getAtributos().contains(obrigatorio)) {
                        throw new SchemaInconsistenteException(
                            nomesTipo,
                            "atributo obrigatório '%s' não está na lista de atributos do tipo".formatted(obrigatorio)
                        );
                    }
                }
            }
        }
    }

    // valida os atributos de uma Entidade
    public void validarEntidade(
            Map<String, Object> entidadeAtributos,
            String tipoEntidade,
            Map<String, AtributoSchema> schemaAtributos,
            Map<String, TipoEntidadeSchema> schemaEntidades) {

        TipoEntidadeSchema tipoSchema = resolverTipo(tipoEntidade, schemaEntidades);
        validarObrigatorios(entidadeAtributos, tipoSchema);
        validarValoresAtributos(entidadeAtributos, schemaAtributos, tipoSchema);
    }

    private TipoEntidadeSchema resolverTipo(String tipo, Map<String, TipoEntidadeSchema> schemaEntidades) {
        TipoEntidadeSchema tipoSchema = schemaEntidades.get(tipo);
        if (tipoSchema == null) {
            throw new TipoEntidadeInvalidoException(tipo);
        }
        return tipoSchema;
    }

    private void validarObrigatorios(Map<String, Object> atributos, TipoEntidadeSchema tipoSchema) {
        if (tipoSchema.getObrigatorios() == null) return;

        for (String obrigatorio : tipoSchema.getObrigatorios()) {
            if (!atributos.containsKey(obrigatorio)) {
                throw new AtributoInvalidoException(
                    obrigatorio,
                    "atributo obrigatório ausente"
                );
            }
        }
    }

    private void validarValoresAtributos(
            Map<String, Object> atributos,
            Map<String, AtributoSchema> schemaAtributos,
            TipoEntidadeSchema tipoSchema
        ) {

        for (Map.Entry<String, Object> entry : atributos.entrySet()) {
            String nomeAtributo = entry.getKey();
            Object valor = entry.getValue();

            AtributoSchema meta = schemaAtributos.get(nomeAtributo);
            if (meta == null) continue;

            if (meta.isDerivado()) {
                throw new AtributoInvalidoException(
                    nomeAtributo,
                    "atributo derivado não pode ser informado manualmente"
                );
            }

            switch (meta.getTipo()) {
                case "int"      -> validarInt(nomeAtributo, valor, meta);
                case "enum"     -> validarEnum(nomeAtributo, valor, meta);
                case "array"    -> validarArray(nomeAtributo, valor, meta);
                case "composto" -> validarComposto(nomeAtributo, valor, meta);
                case "bool"     -> validarBool(nomeAtributo, valor);
            }
        }
    }

    private void validarInt(String nome, Object valor, AtributoSchema meta) {
        if (!(valor instanceof Integer i)) {
            throw new AtributoInvalidoException(nome, "deveria ser um número inteiro");
        }
        if (meta.getMin() != null && i < meta.getMin()) {
            throw new AtributoInvalidoException(
                nome,
                "valor %d abaixo do mínimo permitido (%d)".formatted(i, meta.getMin())
            );
        }
        if (meta.getMax() != null && i > meta.getMax()) {
            throw new AtributoInvalidoException(
                nome,
                "valor %d acima do máximo permitido (%d)".formatted(i, meta.getMax())
            );
        }
    }

    private void validarEnum(String nome, Object valor, AtributoSchema meta) {
        if (!(valor instanceof String s)) {
            throw new AtributoInvalidoException(nome, "deveria ser uma string");
        }
        if (meta.getValoresValidos() != null && !meta.getValoresValidos().contains(s)) {
            throw new AtributoInvalidoException(
                nome,
                "valor '%s' não está entre os permitidos: %s".formatted(s, meta.getValoresValidos())
            );
        }
    }

    private void validarArray(String nome, Object valor, AtributoSchema meta) {
        if (!(valor instanceof List<?> lista)) {
            throw new AtributoInvalidoException(nome, "deveria ser uma lista");
        }
        if (meta.getMinimoItens() != null && lista.size() < meta.getMinimoItens()) {
            throw new AtributoInvalidoException(
                nome,
                "lista tem %d itens, mínimo é %d".formatted(lista.size(), meta.getMinimoItens())
            );
        }
        if (meta.getMaximoItens() != null && lista.size() > meta.getMaximoItens()) {
            throw new AtributoInvalidoException(
                nome,
                "lista tem %d itens, máximo é %d".formatted(lista.size(), meta.getMaximoItens())
            );
        }
        if (meta.getItensValidos() != null) {
            for (Object item : lista) {
                if (!meta.getItensValidos().contains(item.toString())) {
                    throw new AtributoInvalidoException(
                        nome,
                        "item '%s' não está entre os valores permitidos".formatted(item)
                    );
                }
            }
        }
    }

    private void validarComposto(String nome, Object valor, AtributoSchema meta) {
        if (!(valor instanceof Map<?, ?> mapa)) {
            throw new AtributoInvalidoException(nome, "deveria ser um objeto");
        }
        if (meta.getCampos() == null) return;

        for (Map.Entry<String, CampoCompostoSchema> campoEntry : meta.getCampos().entrySet()) {
            String nomeCampo = campoEntry.getKey();
            CampoCompostoSchema campoSchema = campoEntry.getValue();

            if (campoSchema.isDerivado()) continue;

            Object valorCampo = mapa.get(nomeCampo);
            if (valorCampo == null) continue;

            if ("int".equals(campoSchema.getTipo()) && valorCampo instanceof Integer i) {
                if (campoSchema.getMin() != null && i < campoSchema.getMin()) {
                    throw new AtributoInvalidoException(
                        nome + "." + nomeCampo,
                        "valor %d abaixo do mínimo (%d)".formatted(i, campoSchema.getMin())
                    );
                }
                if (campoSchema.getMax() != null && i > campoSchema.getMax()) {
                    throw new AtributoInvalidoException(
                        nome + "." + nomeCampo,
                        "valor %d acima do máximo (%d)".formatted(i, campoSchema.getMax())
                    );
                }
            }
        }
    }

    private void validarBool(String nome, Object valor) {
        if (!(valor instanceof Boolean)) {
            throw new AtributoInvalidoException(nome, "deveria ser true ou false");
        }
    }
}