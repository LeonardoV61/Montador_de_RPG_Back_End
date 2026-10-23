package com.draftstd.hydraforge.domain.service.engine.components;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormulaReferencer {

    public static void processarEExecutar(String formulaBruta, Map<String, Object> atributosAtacante, Map<String, Object> atributosAlvo) {
        
        Pattern pattern = Pattern.compile("@(atacante|alvo)\\.(\\w+)");
        Matcher matcher = pattern.matcher(formulaBruta);
        
        StringBuilder formulaNumerica = new StringBuilder();
        
        while (matcher.find()) {
            String escopo = matcher.group(1);   // "atacante" ou "alvo"
            String atributo = matcher.group(2); // "forca", "defesa", etc.
            
            Object valorObj = "atacante".equals(escopo) 
                    ? (atributosAtacante != null ? atributosAtacante.get(atributo) : null) 
                    : (atributosAlvo != null ? atributosAlvo.get(atributo) : null);
            
            Double valor = 0.0;
            
            if (valorObj instanceof Number n) {
                valor = n.doubleValue();
            } else if (valorObj instanceof String s) {
                try {
                    valor = Double.parseDouble(s);
                } catch (NumberFormatException e) {
                    valor = 0.0;
                }
            }
            
            matcher.appendReplacement(formulaNumerica, String.valueOf(valor));
        }
        matcher.appendTail(formulaNumerica);
        
        FormulaSolver.execute(formulaNumerica.toString());
    }
}