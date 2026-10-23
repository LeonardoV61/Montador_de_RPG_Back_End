package com.draftstd.hydraforge.domain.service.engine.utils.interpretador;

import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto.Alvo;

import java.util.List;
import java.util.Map;

public class ResultadoExpressao {

    public enum TipoResultado {
        NUMERO, BOOLEANO, TEXTO, ALVO, INSTRUCAO, LISTA, OBJETO, NULO
    }

    private final TipoResultado tipo;
    private final Object valor;

    private ResultadoExpressao(TipoResultado tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public static ResultadoExpressao numero(double valor) {
        return new ResultadoExpressao(TipoResultado.NUMERO, valor);
    }

    public static ResultadoExpressao booleano(boolean valor) {
        return new ResultadoExpressao(TipoResultado.BOOLEANO, valor);
    }

    public static ResultadoExpressao texto(String valor) {
        return new ResultadoExpressao(TipoResultado.TEXTO, valor);
    }

    public static ResultadoExpressao alvo(Alvo alvo) {
        return new ResultadoExpressao(TipoResultado.ALVO, alvo);
    }

    public static ResultadoExpressao instrucao(Object expressao) {
        return new ResultadoExpressao(TipoResultado.INSTRUCAO, expressao);
    }

    public static ResultadoExpressao nulo() {
        return new ResultadoExpressao(TipoResultado.NULO, null);
    }

    public static ResultadoExpressao lista(List<?> valor) {
        return new ResultadoExpressao(TipoResultado.LISTA, valor);
    }

    public static ResultadoExpressao objeto(Object valor) {
        return new ResultadoExpressao(TipoResultado.OBJETO, valor);
    }

    // Getters com coerção defensiva
    public double comoNumero() {
        if (valor instanceof Number n) {
            return n.doubleValue();
        }
        throw new IllegalStateException("Resultado é " + tipo + ", não NUMERO");
    }

    public boolean comoBooleano() {
        if (valor instanceof Boolean b) {
            return b;
        }
        throw new IllegalStateException("Resultado é " + tipo + ", não BOOLEANO");
    }

    public String comoTexto() {
        if (valor != null) {
            return valor.toString();
        }
        throw new IllegalStateException("Resultado é " + tipo + ", não TEXTO");
    }

    public List<?> comoLista() {
        if (valor instanceof List<?> l) {
            return l;
        }
        throw new IllegalStateException("Resultado é " + tipo + ", não LISTA");
    }

    public Alvo comoAlvo() {
        if (valor instanceof Alvo a) {
            return a;
        }
        throw new IllegalStateException("Resultado é " + tipo + ", não ALVO");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> comoInstrucao() {
        if (valor instanceof Map<?, ?> m) {
            return (Map<String, Object>) m;
        }
        throw new IllegalStateException("Resultado é " + tipo + ", não INSTRUCAO");
    }

    public Object comoObjeto() {
        return valor;
    }

    public TipoResultado getTipo() { return tipo; }
    
    public Object getValor() { return valor; }

    @Override
    public String toString() {
        return "ResultadoExpressao{tipo=" + tipo + ", valor=" + valor + "}";
    }
}