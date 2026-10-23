package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.engine.utils.ColetaParcialUtil;
import com.draftstd.hydraforge.domain.service.engine.utils.FonteListaResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ColetarDeclaracoesParalelasHandler implements EtapaHandler {

    private final FonteListaResolver fonteListaResolver;
    private final JsonMapper mapper;

    @Override
    public String tipoEtapa() { return "COLETAR_DECLARACOES_PARALELAS"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();

        String salvarEm   = exigirTexto(params, "salvar_em", etapa);
        String prompt     = (String) params.getOrDefault("prompt", "Declare your action");
        boolean podePasar = params.containsKey("pode_passar") ? (Boolean) params.get("pode_passar") : true;

        boolean grupoFiltroVivo = params.containsKey("grupo_filtro_vivo") && (Boolean) params.get("grupo_filtro_vivo");
        String grupoAtributoVida = (String) params.getOrDefault("grupo_atributo_vida", "vigour");

        Object rawGrupoEstatico = params.get("grupo_estatico");
        JsonNode grupoEstaticoNode = rawGrupoEstatico instanceof JsonNode node ? node : mapper.valueToTree(rawGrupoEstatico);

        List<UUID> idsEsperados = fonteListaResolver.resolverIds(
                exigirTexto(params, "grupo_fonte", etapa),
                grupoEstaticoNode,
                ctx,
                grupoFiltroVivo,
                grupoAtributoVida
        );

        Map<UUID, Object> parcial = ColetaParcialUtil.carregarParcial(ctx, salvarEm);
        parcial = ColetaParcialUtil.registrarResposta(ctx, salvarEm, parcial, idsEsperados);

        List<UUID> pendentes = ColetaParcialUtil.pendentes(idsEsperados, parcial);

        if (pendentes.isEmpty()) {
            ColetaParcialUtil.finalizarColeta(ctx, salvarEm, parcial);
            return ResultadoEtapa.concluida(Map.of(
                    "totalRespondentes", parcial.size(), salvarEm, parcial));
        }

        boolean opcoesFiltroVivo = params.containsKey("opcoes_filtro_vivo") && (Boolean) params.get("opcoes_filtro_vivo");
        String opcoesAtributoVida = (String) params.getOrDefault("opcoes_atributo_vida", "vigour");

        Object rawOpcoesEstatico = params.get("opcoes_estatico");
        JsonNode opcoesEstaticoNode = rawOpcoesEstatico instanceof JsonNode node ? node : mapper.valueToTree(rawOpcoesEstatico);

        List<String> opcoes = fonteListaResolver.resolverIdsComoTexto(
                exigirTexto(params, "opcoes_fonte", etapa),
                opcoesEstaticoNode,
                ctx,
                opcoesFiltroVivo,
                opcoesAtributoVida
        );

        return ResultadoEtapa.aguardandoInputMultiplo(Map.of(
                "prompt",        prompt,
                "opcoes",        opcoes,
                "podePassar",    podePasar,
                "salvarEm",      salvarEm + "_entrada",
                "respondidos",   new ArrayList<>(parcial.keySet()),
                "pendentes",     pendentes,
                "totalEsperado", idsEsperados.size()
        ));
    }

    private String exigirTexto(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object val = params.get(chave);
        if (val == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return val.toString();
    }
}