package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.domain.service.engine.components.InterpretadorJson;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InterpretadorContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class SolicitadorRolagemHandler implements EtapaHandler {

    private final InterpretadorJson interpretador;
    private final InstanciaResolver instanciaResolver;
    private final CenaPersistencePort cenaPort;
    private final JsonMapper mapper;

    @Override
    public String tipoEtapa() { return "SOLICITAR_ROLAGEM"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        Object rawCampoPedido = params.get("campo_pedido");
        String campoPedido = rawCampoPedido != null ? rawCampoPedido.toString() : "Role os dados";

        Object rawPodePassar = params.get("pode_passar");
        boolean podePassar = rawPodePassar instanceof Boolean b ? b
                : (rawPodePassar != null && Boolean.parseBoolean(rawPodePassar.toString()));

        String salvarEm = exigirTexto(params, "salvar_em", etapa);
        
        if (ctx.getContexto().containsKey(salvarEm)) {
            Object valor = ctx.getContexto().get(salvarEm, Object.class).orElse(null);
            return ResultadoEtapa.concluida(Map.of(
                "campoPedido", campoPedido,
                "escolha", valor != null ? valor.toString() : "",
                "rolos", ctx.getContexto().get(salvarEm + "_rolos", Object.class).orElse(List.of())
            ));
        }

        String formula = resolverFormula(params, ctx, etapa);
        Map<String, Object> rolagemConfig = parseFormula(formula);

        return ResultadoEtapa.aguardandoInput(Map.of(
            "campoPedido", campoPedido,
            "salvar_em", salvarEm,
            "pode_passar", podePassar,
            "rolagem", rolagemConfig
        ));
    }

    private String resolverFormula(Map<String, Object> params, ExecucaoContexto ctx, EtapaExecutavel etapa) {
        // 1. Fórmulas estáticas (ex: "1d4")
        if (params.get("formula") != null) {
            return params.get("formula").toString();
        }

        // 2. Fórmulas dinâmicas (ex: expressão que busca dano na arma do personagem)
        if (params.get("expressao_formula") != null) {
            InterpretadorContexto intCtx = new InterpretadorContexto(
                (ProcedimentoContexto) ctx, 
                instanciaResolver, 
                cenaPort, 
                mapper
            );
            
            JsonNode expressaoNode = mapper.valueToTree(params.get("expressao_formula"));
            return interpretador.interpretar(expressaoNode, intCtx).comoTexto();
        }

        // 3. Fallback (Configurações do procedimento)
        Object rawChaveConfgs = params.get("chave_confgs");
        String chaveConfgs = rawChaveConfgs != null ? rawChaveConfgs.toString() : null;
        if (chaveConfgs != null) {
            String variacaoDados = ctx.getContexto().getStringOrThrow("start_type");
            Procedimento procedimento = ctx.getProcedimento();
            
            Map<String, Object> confgsGeralMap = procedimento.getConfigsGeral();
            JsonNode confgsGeral = confgsGeralMap != null ? mapper.valueToTree(confgsGeralMap) : mapper.createObjectNode();
            
            JsonNode tipoNode = confgsGeral.path("start_types").path(variacaoDados);
            if (tipoNode.isMissingNode())
                throw new IllegalArgumentException("Tipo de início desconhecido: " + variacaoDados);

            String formula = tipoNode.path(chaveConfgs).asText(null);
            if (formula == null || formula.isBlank())
                throw new IllegalArgumentException("Fórmula '" + chaveConfgs + "' não encontrada para " + variacaoDados);
            
            return formula;
        }

        throw new IllegalArgumentException("Nenhuma fórmula, expressão ou chave_confgs fornecida na etapa.");
    }

    private Map<String, Object> parseFormula(String formula) {
        String limpa = formula.replaceAll("\\s+", "");
        Map<String, Object> resultado = new LinkedHashMap<>();

        Pattern somaPattern = Pattern.compile("^(d\\d+)\\+(\\d+|d\\d+)$");
        Matcher somaMatcher = somaPattern.matcher(limpa);
        if (somaMatcher.matches()) {
            String dado1 = somaMatcher.group(1);
            String termo2 = somaMatcher.group(2);
            List<String> dados = new ArrayList<>();
            dados.add(dado1);
            if (termo2.startsWith("d")) {
                dados.add(termo2);
                resultado.put("dados", dados);
            } else {
                resultado.put("dados", dados);
                resultado.put("modificador", Integer.parseInt(termo2));
            }
            return resultado;
        }

        Pattern multPattern = Pattern.compile("^(\\d+)d(\\d+)$");
        Matcher multMatcher = multPattern.matcher(limpa);
        if (multMatcher.matches()) {
            int qtd = Integer.parseInt(multMatcher.group(1));
            String dado = "d" + multMatcher.group(2);
            List<String> dados = new ArrayList<>();
            for (int i = 0; i < qtd; i++) dados.add(dado);
            resultado.put("dados", dados);
            return resultado;
        }

        if (limpa.matches("^d\\d+$")) {
            resultado.put("dados", List.of(limpa));
            return resultado;
        }

        throw new IllegalArgumentException("Fórmula não suportada: " + formula);
    }

    private String exigirTexto(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object val = params.get(chave);
        if (val == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return val.toString();
    }
}