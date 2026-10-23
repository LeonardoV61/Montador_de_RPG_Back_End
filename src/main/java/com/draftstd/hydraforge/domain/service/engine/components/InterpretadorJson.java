package com.draftstd.hydraforge.domain.service.engine.components;

import com.draftstd.hydraforge.ports.out.persistence.mecanica.ResolucaoOutputPort;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto.Alvo;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto.Contexto;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.ResultadoExpressao;
import com.draftstd.hydraforge.domain.model.mecanica.Resolucao;
import com.draftstd.hydraforge.domain.service.mecanica.ResolutionEvaluator;
import com.draftstd.hydraforge.domain.service.mecanica.ResolutionOutcome;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.*;

@Component
public class InterpretadorJson {

    private final ResolucaoOutputPort resolucaoPort;
    private final ResolutionEvaluator resolutionEvaluator;
    private final JsonMapper jsonMapper;

    public InterpretadorJson(ResolucaoOutputPort resolucaoPort, ResolutionEvaluator resolutionEvaluator, JsonMapper jsonMapper) {
        this.resolucaoPort = resolucaoPort;
        this.resolutionEvaluator = resolutionEvaluator;
        this.jsonMapper = jsonMapper;
    }

    public ResultadoExpressao interpretar(Object node, Contexto contexto) {
        if (node == null) return ResultadoExpressao.nulo();

        if (node instanceof Number n) return ResultadoExpressao.numero(n.doubleValue());
        if (node instanceof Boolean b) return ResultadoExpressao.booleano(b);
        if (node instanceof String s) return ResultadoExpressao.texto(s);

        if (!(node instanceof Map<?, ?> mapRaw)) {
            return ResultadoExpressao.objeto(node);
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> expr = (Map<String, Object>) mapRaw;
        String tipo = (String) expr.get("tipo");

        if (tipo == null) {
            return ResultadoExpressao.objeto(expr);
        }

        return switch (tipo) {
            case "constante"         -> resolverConstante(expr);
            case "caminho"           -> resolverCaminho(expr, contexto);
            case "caminho_coringa"   -> resolverCaminhoCoringa(expr, contexto);
            case "formula"           -> resolverFormula(expr, contexto);
            case "formula_raw"       -> resolverFormulaRaw(expr);
            case "condicao"          -> resolverCondicao(expr, contexto);
            case "condicao_composta" -> resolverCondicaoComposta(expr, contexto);
            case "ternario"          -> resolverTernario(expr, contexto);
            case "filtro"            -> resolverFiltro(expr, contexto);
            case "funcao"            -> resolverFuncao(expr, contexto);
            case "alvo"              -> resolverAlvo(expr, contexto);
            case "texto"             -> resolverTexto(expr, contexto);
            case "instrucao"         -> resolverInstrucao(expr);
            case "objeto"            -> resolverObjeto(expr);
            default -> throw new IllegalArgumentException("Tipo de expressão desconhecido: '" + tipo + "'");
        };
    }

    private ResultadoExpressao resolverConstante(Map<String, Object> expr) {
        Object valor = expr.get("valor");
        if (valor == null) return ResultadoExpressao.nulo();
        if (valor instanceof List<?> l) return ResultadoExpressao.lista(l);
        if (valor instanceof Number n) return ResultadoExpressao.numero(n.doubleValue());
        if (valor instanceof Boolean b) return ResultadoExpressao.booleano(b);
        return ResultadoExpressao.texto(valor.toString());
    }

    private ResultadoExpressao resolverCaminho(Map<String, Object> expr, Contexto ctx) {
        String caminho = (String) expr.get("caminho");
        if (caminho == null) {
            throw new IllegalArgumentException("'caminho' é obrigatório para tipo 'caminho'");
        }
        Optional<Object> optValor = ctx.get(caminho);
        if (optValor.isEmpty()) {
            if (expr.containsKey("padrao")) {
                return interpretar(expr.get("padrao"), ctx);
            }
            throw new IllegalArgumentException("Caminho não encontrado: " + caminho);
        }
        return converterParaResultado(optValor.get());
    }

    private ResultadoExpressao resolverCaminhoCoringa(Map<String, Object> expr, Contexto ctx) {
        String base = (String) expr.get("base");
        String prop = (String) expr.get("propriedade");
        if (base == null || prop == null) {
            throw new IllegalArgumentException("'base' e 'propriedade' são obrigatórios em caminho_coringa");
        }
        Optional<Object> optBase = ctx.get(base);
        if (optBase.isEmpty()) {
            return ResultadoExpressao.lista(Collections.emptyList());
        }
        if (!(optBase.get() instanceof List<?> lista)) {
            throw new IllegalArgumentException("Caminho base '" + base + "' não é uma lista");
        }
        List<Object> resultados = new ArrayList<>();
        for (Object item : lista) {
            if (item instanceof Map<?, ?> map) {
                resultados.add(map.get(prop));
            }
        }
        return ResultadoExpressao.lista(resultados);
    }

    private ResultadoExpressao resolverFormula(Map<String, Object> expr, Contexto ctx) {
        Object aNode = expr.get("operandoA");
        Object bNode = expr.get("operandoB");
        String op = (String) expr.get("operador");
        if (aNode == null || bNode == null || op == null) {
            throw new IllegalArgumentException("'operandoA', 'operandoB' e 'operador' são obrigatórios na fórmula");
        }
        double a = interpretar(aNode, ctx).comoNumero();
        double b = interpretar(bNode, ctx).comoNumero();
        double resultado = switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            case "%" -> a % b;
            case "^" -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Operador inválido: " + op);
        };
        return ResultadoExpressao.numero(resultado);
    }

    private ResultadoExpressao resolverFormulaRaw(Map<String, Object> expr) {
        String formula = (String) expr.get("expressao");
        if (formula == null) {
            throw new IllegalArgumentException("'expressao' é obrigatório em formula_raw");
        }
        double resultado = FormulaSolver.execute(formula);
        return ResultadoExpressao.numero(resultado);
    }

    private ResultadoExpressao resolverCondicao(Map<String, Object> expr, Contexto ctx) {
        Object aNode = expr.get("operandoA");
        Object bNode = expr.get("operandoB");
        String op = (String) expr.get("operador");
        if (aNode == null || bNode == null || op == null) {
            throw new IllegalArgumentException("'operandoA', 'operandoB' e 'operador' são obrigatórios na condição");
        }
        ResultadoExpressao resA = interpretar(aNode, ctx);
        ResultadoExpressao resB = interpretar(bNode, ctx);

        if (resA.getTipo() == ResultadoExpressao.TipoResultado.NUMERO &&
            resB.getTipo() == ResultadoExpressao.TipoResultado.NUMERO) {
            double a = resA.comoNumero();
            double b = resB.comoNumero();
            boolean r = switch (op) {
                case "<"  -> a < b;
                case ">"  -> a > b;
                case "<=" -> a <= b;
                case ">=" -> a >= b;
                case "==" -> a == b;
                case "!=" -> a != b;
                default -> throw new IllegalArgumentException("Operador numérico inválido: " + op);
            };
            return ResultadoExpressao.booleano(r);
        }

        String a = resA.getValor() != null ? resA.getValor().toString() : "";
        String b = resB.getValor() != null ? resB.getValor().toString() : "";
        boolean r = switch (op) {
            case "==" -> a.equals(b);
            case "!=" -> !a.equals(b);
            default -> throw new IllegalArgumentException("Texto só suporta == e !=, recebido: " + op);
        };
        return ResultadoExpressao.booleano(r);
    }

    private ResultadoExpressao resolverCondicaoComposta(Map<String, Object> expr, Contexto ctx) {
        String op = (String) expr.get("operador");
        @SuppressWarnings("unchecked")
        List<Object> conds = (List<Object>) expr.get("condicoes");
        if (op == null || conds == null || conds.isEmpty()) {
            throw new IllegalArgumentException("'operador' e 'condicoes' (array não vazio) são obrigatórios");
        }

        boolean resultado;
        if ("E".equalsIgnoreCase(op) || "AND".equalsIgnoreCase(op)) {
            resultado = conds.stream().allMatch(c -> interpretar(c, ctx).comoBooleano());
        } else if ("OU".equalsIgnoreCase(op) || "OR".equalsIgnoreCase(op)) {
            resultado = conds.stream().anyMatch(c -> interpretar(c, ctx).comoBooleano());
        } else {
            throw new IllegalArgumentException("Operador composto deve ser 'E' ou 'OU'");
        }
        return ResultadoExpressao.booleano(resultado);
    }

    private ResultadoExpressao resolverTernario(Map<String, Object> expr, Contexto ctx) {
        Object condNode = expr.get("condicao");
        Object thenNode = expr.get("entao");
        Object elseNode = expr.get("senao");
        if (condNode == null || thenNode == null || elseNode == null) {
            throw new IllegalArgumentException("'condicao', 'entao' e 'senao' são obrigatórios no ternário");
        }
        boolean cond = interpretar(condNode, ctx).comoBooleano();
        return interpretar(cond ? thenNode : elseNode, ctx);
    }

    private ResultadoExpressao resolverFiltro(Map<String, Object> expr, Contexto ctx) {
        Object listaNode = expr.get("lista");
        Object condNode = expr.get("condicao");
        if (listaNode == null || condNode == null) {
            throw new IllegalArgumentException("'lista' e 'condicao' são obrigatórios no filtro");
        }
        ResultadoExpressao listaRes = interpretar(listaNode, ctx);
        Object rawLista = listaRes.getValor();
        if (!(rawLista instanceof List<?> lista)) {
            throw new IllegalArgumentException("Filtro requer uma lista");
        }
        List<Object> resultado = new ArrayList<>();
        for (Object item : lista) {
            Contexto itemCtx = criarContextoItem(item);
            if (interpretar(condNode, itemCtx).comoBooleano()) {
                resultado.add(item);
            }
        }
        return ResultadoExpressao.lista(resultado);
    }

    private Contexto criarContextoItem(Object item) {
        return caminho -> {
            if (!caminho.startsWith("item")) return Optional.empty();
            if (caminho.equals("item")) return Optional.ofNullable(item);
            String resto = caminho.substring(5);
            if (resto.isEmpty()) return Optional.ofNullable(item);
            if (item instanceof Map<?, ?> map) {
                return Optional.ofNullable(resolverCaminhoEmMap(map, resto));
            }
            return Optional.empty();
        };
    }

    private Object resolverCaminhoEmMap(Map<?, ?> map, String caminho) {
        String[] partes = caminho.split("\\.");
        Object atual = map;
        for (String parte : partes) {
            if (atual instanceof Map<?, ?> m) {
                atual = m.get(parte);
            } else {
                return null;
            }
        }
        return atual;
    }

    private ResultadoExpressao resolverFuncao(Map<String, Object> expr, Contexto ctx) {
        String nome = (String) expr.get("nome");
        @SuppressWarnings("unchecked")
        List<Object> argsNode = (List<Object>) expr.get("argumentos");
        if (nome == null || argsNode == null) {
            throw new IllegalArgumentException("'nome' e 'argumentos' são obrigatórios na função");
        }
        List<ResultadoExpressao> args = new ArrayList<>();
        for (Object arg : argsNode) {
            args.add(interpretar(arg, ctx));
        }

        return switch (nome) {
            case "ceil"  -> ResultadoExpressao.numero(Math.ceil(args.get(0).comoNumero()));
            case "floor" -> ResultadoExpressao.numero(Math.floor(args.get(0).comoNumero()));
            case "round" -> ResultadoExpressao.numero(Math.round(args.get(0).comoNumero()));
            case "abs"   -> ResultadoExpressao.numero(Math.abs(args.get(0).comoNumero()));
            case "min"   -> ResultadoExpressao.numero(Math.min(args.get(0).comoNumero(), args.get(1).comoNumero()));
            case "max"   -> ResultadoExpressao.numero(Math.max(args.get(0).comoNumero(), args.get(1).comoNumero()));
            case "clamp" -> {
                double v = args.get(0).comoNumero();
                double mn = args.get(1).comoNumero();
                double mx = args.get(2).comoNumero();
                yield ResultadoExpressao.numero(Math.max(mn, Math.min(mx, v)));
            }
            case "size" -> {
                List<?> l = args.get(0).comoLista();
                yield ResultadoExpressao.numero(l.size());
            }
            case "sumList" -> {
                List<?> l = args.get(0).comoLista();
                Object mapExpr = argsNode.get(1);
                double soma = 0;
                for (Object item : l) {
                    soma += interpretar(mapExpr, criarContextoItem(item)).comoNumero();
                }
                yield ResultadoExpressao.numero(soma);
            }
            case "averageList" -> {
                List<?> l = args.get(0).comoLista();
                if (l.isEmpty()) yield ResultadoExpressao.numero(0);
                Object mapExpr = argsNode.get(1);
                double soma = 0;
                for (Object item : l) {
                    soma += interpretar(mapExpr, criarContextoItem(item)).comoNumero();
                }
                yield ResultadoExpressao.numero(soma / l.size());
            }
            case "resolver" -> {
                UUID idResolucao = UUID.fromString(args.get(0).getValor().toString());
                Object contextoObj = args.get(1).getValor();
                Resolucao resolucao = resolucaoPort.buscarPorId(idResolucao)
                        .orElseThrow(() -> new IllegalArgumentException("Resolução não encontrada: " + idResolucao));
                ResolutionOutcome outcome = resolutionEvaluator.evaluate(resolucao, jsonMapper.valueToTree(contextoObj));
                Map<String, Object> objeto = new LinkedHashMap<>();
                objeto.put("roll", outcome.roll());
                objeto.put("targetValue", outcome.targetValue());
                objeto.put("success", outcome.success());
                objeto.put("motivo", outcome.motivo());
                objeto.put("detalhes", jsonMapper.convertValue(outcome.detalhes(), Map.class));
                yield ResultadoExpressao.objeto(objeto);
            }
            case "if"       -> args.get(0).comoBooleano() ? args.get(1) : args.get(2);
            case "contains" -> ResultadoExpressao.booleano(args.get(0).comoLista().contains(args.get(1).getValor()));
            case "concat"   -> {
                StringBuilder sb = new StringBuilder();
                for (ResultadoExpressao a : args) sb.append(a.getValor() != null ? a.getValor().toString() : "");
                yield ResultadoExpressao.texto(sb.toString());
            }
            case "ifNull" -> args.get(0).getTipo() == ResultadoExpressao.TipoResultado.NULO ? args.get(1) : args.get(0);
            default -> throw new IllegalArgumentException("Função desconhecida: " + nome);
        };
    }

    private ResultadoExpressao resolverAlvo(Map<String, Object> expr, Contexto ctx) {
        String tipoEntidade = (String) expr.get("tipoEntidade");
        Object idRaw = expr.get("id");
        Object id = idRaw instanceof Map<?, ?> ? interpretar(idRaw, ctx).getValor() : idRaw;
        return ResultadoExpressao.alvo(new Alvo(tipoEntidade, id));
    }

    private ResultadoExpressao resolverTexto(Map<String, Object> expr, Contexto ctx) {
        String template = (String) expr.get("template");
        if (template == null) throw new IllegalArgumentException("'template' é obrigatório em texto");

        if (expr.get("variaveis") instanceof Map<?, ?> vars) {
            for (Map.Entry<?, ?> entry : vars.entrySet()) {
                String nomeVar = entry.getKey().toString();
                String valor = interpretar(entry.getValue(), ctx).getValor().toString();
                template = template.replace("{" + nomeVar + "}", valor);
            }
        }
        return ResultadoExpressao.texto(template);
    }

    private ResultadoExpressao resolverInstrucao(Map<String, Object> expr) {
        return ResultadoExpressao.instrucao(expr);
    }

    private ResultadoExpressao resolverObjeto(Map<String, Object> expr) {
        Object valor = expr.get("valor");
        return valor != null ? ResultadoExpressao.objeto(valor) : ResultadoExpressao.nulo();
    }

    private ResultadoExpressao converterParaResultado(Object valor) {
        if (valor == null) return ResultadoExpressao.nulo();
        if (valor instanceof Number n) return ResultadoExpressao.numero(n.doubleValue());
        if (valor instanceof String s) return ResultadoExpressao.texto(s);
        if (valor instanceof Boolean b) return ResultadoExpressao.booleano(b);
        if (valor instanceof List<?> l) return ResultadoExpressao.lista(l);
        return ResultadoExpressao.texto(valor.toString());
    }
}