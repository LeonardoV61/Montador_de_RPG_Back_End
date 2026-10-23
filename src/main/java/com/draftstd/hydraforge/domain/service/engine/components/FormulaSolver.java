package com.draftstd.hydraforge.domain.service.engine.components;

import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.Operations;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.Procedures;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FormulaSolver { 

    public static double execute(String formula) {
        List<Object> tokens = Procedures.cloneTokens(formula);
        List<Object> rpn = formulaTranslator(tokens);
        return solver(rpn);
    }

    private static List<Object> formulaTranslator(List<Object> tokens) {
        Stack<String> operadores = new Stack<>();
        List<Object> output = new ArrayList<>();

        for (Object token : tokens) {
            if (token instanceof Double) {
                output.add(token);
            } else {
                String op = (String) token;
                if (op.equals("(")) {
                    operadores.push(op);
                } else if (op.equals(")")) {
                    while (!operadores.isEmpty() && !operadores.peek().equals("(")) {
                        output.add(operadores.pop());
                    }
                    if (!operadores.isEmpty()) {
                        operadores.pop(); // remove "("
                    }
                } else {
                    while (!operadores.isEmpty() && Procedures.priority(operadores.peek()) >= Procedures.priority(op)) {
                        output.add(operadores.pop());
                    }
                    operadores.push(op);
                }
            }
        }
        while (!operadores.isEmpty()) {
            output.add(operadores.pop());
        }
        return output;
    }

    private static double solver(List<Object> tokens) {
        Stack<Double> stack = new Stack<>();
        for (Object token : tokens) {
            if (token instanceof Double d) {
                stack.push(d);
            } else {
                String op = (String) token;
                if (stack.size() < 2) continue;
                double b = stack.pop();
                double a = stack.pop();
                stack.push(Operations.execute(op, a, b));
            }
        }
        return stack.isEmpty() ? 0.0 : stack.pop();
    }
}