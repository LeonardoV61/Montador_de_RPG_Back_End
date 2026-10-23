// domain/validation/schema/TipoEntidadeSchema.java
package com.draftstd.hydraforge.domain.validation.schema;

import java.util.List;

// Representa um tipo dentro de schema_entidades.
// Ex: "criatura" -> quais atributos usa e quais são obrigatórios
public class TipoEntidadeSchema {

    private String label;
    private List<String> atributos;
    private List<String> obrigatorios;

    public TipoEntidadeSchema() {
    }

    public TipoEntidadeSchema(String label, List<String> atributos, List<String> obrigatorios) {
        this.label = label;
        this.atributos = atributos;
        this.obrigatorios = obrigatorios;
    }

    public String getLabel()                { return label; }
    public List<String> getAtributos()      { return atributos; }
    public List<String> getObrigatorios()   { return obrigatorios; }
}