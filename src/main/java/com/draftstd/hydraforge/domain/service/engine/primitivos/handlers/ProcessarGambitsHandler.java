package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.exceptions.EntityNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.engine.utils.ColetaParcialUtil;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProcessarGambitsHandler implements EtapaHandler {

    private final EntidadeInstanciaOutputPort instanciaPort;

    @Override
    public String tipoEtapa() { return "PROCESSAR_GAMBITS"; }

    /**
     * parametros_etapa:
     * {
     *   "gambits_ids_fonte":  "contexto.gambits_aliados_ids",
     *   "gambits_info_fonte": "contexto.gambits_aliados",
     *
     *   "definicoes_gambit": {
     *     "DESARMAR": [{ "primitivo": "GERENCIAR_STATUS",
     *                    "parametros": { "acao": "APLICAR", "status": "desarmado" } }],
     *     "DERRUBAR": [...],
     *     "EMPURRAR": [...],
     *     "INTIMIDAR": [...]
     *   },
     *
     *   "salvar_em": "resultados_gambits_aliados"
     * }
     */
    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        String idsFonte = exigirTexto(params, "gambits_ids_fonte", etapa);
        String salvarEm = exigirTexto(params, "salvar_em", etapa);
        Object rawDefinicoes = params.get("definicoes_gambit");

        List<UUID> idsComGambit = ctx.getContexto().getListaIds(idsFonte.replace("contexto.", ""));

        if (idsComGambit == null || idsComGambit.isEmpty()) {
            return ResultadoEtapa.concluida(Map.of("gambitsProcessados", 0));
        }

        Map<UUID, Object> parcial = ColetaParcialUtil.carregarParcial(ctx, salvarEm);
        parcial = ColetaParcialUtil.registrarResposta(ctx, salvarEm, parcial, idsComGambit);

        List<UUID> pendentes = ColetaParcialUtil.pendentes(idsComGambit, parcial);

        if (!pendentes.isEmpty()) {
            List<String> opcoes = new ArrayList<>();
            if (rawDefinicoes instanceof Map<?, ?> defMap) {
                defMap.keySet().forEach(k -> opcoes.add(String.valueOf(k)));
            }
            opcoes.add("PASSAR");

            return ResultadoEtapa.aguardandoInputMultiplo(Map.of(
                    "prompt",      "You earned a GAMBIT! Choose a bonus action.",
                    "opcoes",      opcoes,
                    "podePassar",  true,
                    "salvarEm",    salvarEm + "_entrada",
                    "pendentes",   pendentes,
                    "respondidos", new ArrayList<>(parcial.keySet())
            ));
        }

        Map<UUID, Object> resultados = new LinkedHashMap<>();
        for (var entry : parcial.entrySet()) {
            UUID idAtacante = entry.getKey();
            String acao = extrairAcao(entry.getValue());
            UUID idAlvo = extrairAlvo(entry.getValue());

            if ("PASSAR".equals(acao) || idAlvo == null) continue;

            Object definicaoAcao = rawDefinicoes instanceof Map<?, ?> defMap ? defMap.get(acao) : null;
            if (definicaoAcao == null) continue; // ação desconhecida — ignorada silenciosamente

            EntidadeInstancia atacante = instanciaPort.buscarPorId(idAtacante)
                    .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, idAtacante));
            EntidadeInstancia alvo = instanciaPort.buscarPorId(idAlvo)
                    .orElseThrow(() -> new EntityNotFoundException(EntidadeInstancia.class, idAlvo));

            // reservado para futuras chamadas de primitivos
        }

        ColetaParcialUtil.finalizarColeta(ctx, salvarEm, parcial);

        return ResultadoEtapa.concluida(Map.of(
                "gambitsProcessados", parcial.size(), "resultados", resultados));
    }

    private String extrairAcao(Object decisao) {
        if (decisao instanceof String s) return s;
        if (decisao instanceof Map<?, ?> m) return String.valueOf(m.get("acao"));
        return "PASSAR";
    }

    private UUID extrairAlvo(Object decisao) {
        if (decisao instanceof Map<?, ?> m) {
            Object rawAlvo = m.get("id_alvo");
            if (rawAlvo != null) {
                return rawAlvo instanceof UUID u ? u : UUID.fromString(rawAlvo.toString());
            }
        }
        return null;
    }

    private String exigirTexto(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object val = params.get(chave);
        if (val == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return val.toString();
    }
}