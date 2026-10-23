package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.primitivos.VerificadorAtributo;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class AlterarAtributoHandler implements EtapaHandler {

    private final InstanciaResolver instanciaResolver;
    private final EntidadeInstanciaOutputPort entidadeInstanciaPort;
    private final VerificadorAtributo verificador;

    public AlterarAtributoHandler(InstanciaResolver instanciaResolver,
                                 EntidadeInstanciaOutputPort entidadeInstanciaPort,
                                 VerificadorAtributo verificador) {
        this.instanciaResolver = instanciaResolver;
        this.entidadeInstanciaPort = entidadeInstanciaPort;
        this.verificador = verificador;
    }

    @Override
    public String tipoEtapa() { return "ALTERAR_ATRIBUTO"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        String atributo = exigirTexto(params, "atributo", etapa);
        String op = (String) params.getOrDefault("operacao", "");

        double qtd = resolverQuantidade(params, ctx, etapa);
        EntidadeInstancia inst = resolverInstancia(params, ctx);

        // Ajuste aqui: getValoresAtributos() em vez de getAtributosAtuais()
        Map<String, Object> atributosAtuais = inst.getValoresAtributos();
        if (atributosAtuais == null || !atributosAtuais.containsKey(atributo)) {
            throw new JsonFieldNotFoundException(atributo, "valoresAtributos da instância " + inst.getId());
        }

        Object valorAtualObj = atributosAtuais.get(atributo);
        double valorAtual = valorAtualObj instanceof Number n ? n.doubleValue() : Double.parseDouble(valorAtualObj.toString());

        double resultadoBruto = switch (op) {
            case "", "set" -> qtd;
            case "soma"    -> valorAtual + qtd;
            case "sub"     -> valorAtual - qtd;
            case "mult"    -> valorAtual * qtd;
            case "div" -> {
                if (qtd == 0) throw new IllegalArgumentException(
                        "Divisão por zero em ALTERAR_ATRIBUTO (etapa: " + etapa.getNome() + ")");
                yield valorAtual / qtd;
            }
            default -> throw new IllegalArgumentException(
                    "Operação desconhecida em ALTERAR_ATRIBUTO: '" + op + "'");
        };

        boolean manterInteiro = !"div".equals(op);

        if (!verificador.verificarAtributo(ctx.getIdSistema(), atributo, resultadoBruto)) {
            return ResultadoEtapa.erro(
                    "Valor inválido para '" + atributo + "': " + resultadoBruto +
                            " (etapa: " + etapa.getNome() + ")");
        }

        Map<String, Object> novosAttrs = new HashMap<>(atributosAtuais);
        if (manterInteiro) {
            novosAttrs.put(atributo, (long) resultadoBruto);
        } else {
            novosAttrs.put(atributo, resultadoBruto);
        }
        
        // Ajuste aqui: setValoresAtributos()
        inst.setValoresAtributos(novosAttrs);
        entidadeInstanciaPort.salvar(inst);

        return ResultadoEtapa.concluida(Map.of(
                "idInstancia", inst.getId(),
                "atributo",    atributo,
                "valorAnterior", valorAtual,
                "valorNovo",     manterInteiro ? (long) resultadoBruto : resultadoBruto
        ));
    }

    private double resolverQuantidade(Map<String, Object> params, ExecucaoContexto ctx, EtapaExecutavel etapa) {
        if (params.containsKey("source_key")) {
            String sourceKey = (String) params.get("source_key");
            Object raw = ctx.getContexto().get(sourceKey, Object.class).orElse(null);
            if (raw == null) {
                throw new JsonFieldNotFoundException(sourceKey, etapa.getNome());
            }
            try {
                return Double.parseDouble(raw.toString().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                    "Valor '" + raw + "' em '" + sourceKey + "' não é numérico (etapa: " + etapa.getNome() + ")"
                );
            }
        }
        if (params.containsKey("quantidade")) {
            Object qtdObj = params.get("quantidade");
            return qtdObj instanceof Number n ? n.doubleValue() : Double.parseDouble(qtdObj.toString());
        }
        throw new JsonFieldNotFoundException("quantidade ou source_key", etapa.getNome());
    }

    private EntidadeInstancia resolverInstancia(Map<String, Object> params, ExecucaoContexto ctx) {
        Object idObj = params.get("id_entidade");
        if (idObj == null) {
            return instanciaResolver.retornarAtiva(ctx);
        }
        UUID idEntidade = idObj instanceof UUID u ? u : UUID.fromString(idObj.toString());
        return entidadeInstanciaPort.buscarPorId(idEntidade)
                .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, idEntidade));
    }

    private String exigirTexto(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object val = params.get(chave);
        if (val == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return val.toString();
    }
}