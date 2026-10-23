package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.service.engine.components.ParametroResolver;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.engine.utils.rolagemEngine.ResultadoRolagem;
import com.draftstd.hydraforge.domain.service.engine.utils.rolagemEngine.RolagemEngine;
import com.draftstd.hydraforge.domain.service.engine.utils.rolagemEngine.VantagemTipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RolarSimplesHandler implements EtapaHandler {

    private final RolagemEngine rolagemEngine;
    private final ParametroResolver resolver;
    private final JsonMapper mapper;

    @Override
    public String tipoEtapa() { return "ROLAR_SIMPLES"; }

    /**
     * parametros_etapa:
     * {
     *   "dado": "d20", "quantidade": 1, "explosao": false,
     *   "vantagem": "NORMAL" | "VANTAGEM" | "DESVANTAGEM",
     *
     *   "modificador": {"fonte": "atributo", "chave": "forca"}, // opcional
     *
     *   "modo_sucesso": "AUSENTE" | "ROLAR_IGUAL_OU_MAIOR" | "ROLAR_IGUAL_OU_MENOR",
     *   "valor_alvo": {"fonte": "fixo", "chave": 15 }
     *               | {"fonte": "atributo", "chave": "VIG"}, // Save MB: rolar <= VIG
     *
     *   "salvar_em": "resultado_roll"
     * }
     */
    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        String dado = exigirTexto(params, "dado", etapa);
        
        Object rawQtd = params.get("quantidade");
        int quantidade = rawQtd instanceof Number n ? n.intValue() 
                : (rawQtd != null ? Integer.parseInt(rawQtd.toString()) : 1);

        Object rawExplosao = params.get("explosao");
        boolean explosao = rawExplosao instanceof Boolean b ? b 
                : (rawExplosao != null && Boolean.parseBoolean(rawExplosao.toString()));

        Object rawVantagem = params.get("vantagem");
        VantagemTipo vantagem = VantagemTipo.valueOf(rawVantagem != null ? rawVantagem.toString() : "NORMAL");

        Object rawSalvarEm = params.get("salvar_em");
        String salvarEm = rawSalvarEm != null ? rawSalvarEm.toString() : null;

        ResultadoRolagem rolo = rolagemEngine.executarComVantagem(
                RolagemEngine.simples(dado, quantidade, explosao), vantagem);

        Object rawModificador = params.get("modificador");
        JsonNode nodeModificador = rawModificador != null ? mapper.valueToTree(rawModificador) : null;
        double modificador = resolver.resolverNumero(nodeModificador, ctx);
        
        double total = rolo.total() + modificador;

        int faces = rolagemEngine.parseFaces(dado);
        boolean critico       = quantidade == 1 && rolo.isCritico(faces);
        boolean falhaCritica  = quantidade == 1 && rolo.isFalhaCritica();

        Boolean sucesso = avaliarSucesso(params, ctx, total, etapa);

        if (salvarEm != null) {
            ctx.getContexto().put(salvarEm, (long) total);
            ctx.getContexto().put(salvarEm + "_rolos", rolo.rolos());
            if (sucesso != null) ctx.getContexto().put(salvarEm + "_sucesso", sucesso);
        }

        Map<String, Object> dados = new LinkedHashMap<>();
        dados.put("dado", dado);
        dados.put("rolos", rolo.rolos());
        dados.put("modificador", modificador);
        dados.put("total", total);
        dados.put("critico", critico);
        dados.put("falhaCritica", falhaCritica);
        if (sucesso != null) dados.put("sucesso", sucesso);

        return ResultadoEtapa.concluida(dados);
    }

    private Boolean avaliarSucesso(Map<String, Object> params, ExecucaoContexto ctx, double total, EtapaExecutavel etapa) {
        Object rawModo = params.get("modo_sucesso");
        String modo = rawModo != null ? rawModo.toString() : "AUSENTE";
        if ("AUSENTE".equals(modo)) return null;

        Object rawValorAlvo = params.get("valor_alvo");
        if (rawValorAlvo == null) throw new JsonFieldNotFoundException("valor_alvo", etapa.getNome());

        JsonNode nodeValorAlvo = mapper.valueToTree(rawValorAlvo);
        double alvo = resolver.resolverNumero(nodeValorAlvo, ctx);

        return switch (modo) {
            case "ROLAR_IGUAL_OU_MAIOR" -> total >= alvo;   // d20 vs CA/CD — estilo D&D
            case "ROLAR_IGUAL_OU_MENOR" -> total <= alvo;   // Save de Mythic Bastionland
            default -> throw new IllegalArgumentException("modo_sucesso desconhecido: " + modo);
        };
    }

    private String exigirTexto(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object val = params.get(chave);
        if (val == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return val.toString();
    }
}