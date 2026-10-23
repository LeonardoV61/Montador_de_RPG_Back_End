// domain/validation/schema/CampoCompostoSchema.java
package com.draftstd.hydraforge.domain.validation.schema;

// Representa um campo dentro de um atributo composto.
// Ex: dentro de "for" existem "valor" e "modificador"
public class CampoCompostoSchema {

    private String tipo;
    private boolean derivado;
    private String formula;
    private Integer min;
    private Integer max;
    private Object defaultVal;

    public CampoCompostoSchema() {
    }

    public CampoCompostoSchema(String tipo, boolean derivado, String formula, Integer min, Integer max, Object defaultVal) {
        this.tipo = tipo;
        this.derivado = derivado;
        this.formula = formula;
        this.min = min;
        this.max = max;
        this.defaultVal = defaultVal;
    }

    public String getTipo()      { return tipo; }
    public boolean isDerivado()   { return derivado; }
    public String getFormula()   { return formula; }
    public Integer getMin()      { return min; }
    public Integer getMax()      { return max; }
    public Object getDefaultVal(){ return defaultVal; }
}