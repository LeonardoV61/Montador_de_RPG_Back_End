package com.draftstd.hydraforge.domain.validation.exception;

public class SchemaInconsistenteException extends ValidacaoSchemaException {

    public SchemaInconsistenteException(String tipoEntidade, String atributoAusente) {
        super(
            "schema_entidades." + tipoEntidade,
            "O tipo '%s' referencia o atributo '%s' que não existe em schema_atributos"
            .formatted(tipoEntidade, atributoAusente)
        );
    }

}
