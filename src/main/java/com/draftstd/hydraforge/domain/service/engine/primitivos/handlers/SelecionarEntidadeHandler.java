package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeSistema;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.adapters.out.repository.entidade.EntidadeSistemaRepository;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.JsonNode;


@Component
@RequiredArgsConstructor
public class SelecionarEntidadeHandler implements EtapaHandler {

    private final EntidadeSistemaRepository repo;

    @Override
    public String tipoEtapa() { return "SELECIONAR_ENTIDADE"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        JsonNode params = etapa.getParametrosEtapa();
        String tipo = params.get("tipo_entidade").asString();
        String salvarEm = params.get("salvar_em").asString();
        String modoEsperado = params.path("modo_esperado").asString(null);
        
        if (modoEsperado != null) {
            String modoAtual = ctx.getContexto().getStringOrThrow("modo_escolha_cavaleiro");
            if (!modoEsperado.equals(modoAtual)) {
                return ResultadoEtapa.concluida(Map.of("pulado", true));
            }
        }

        if (ctx.getContexto().containsKey(salvarEm)) {
            UUID id = UUID.fromString(ctx.getContexto().getStringOrThrow(salvarEm));
            return ResultadoEtapa.concluida(Map.of(salvarEm, id));
        }

        List<EntidadeSistema> entidades = repo.findBySistemaIdAndTipo(ctx.getIdSistema(), tipo);

        List<Map<String, Object>> opcoes = new ArrayList<>();
        for (EntidadeSistema e : entidades) {
            Map<String, Object> op = new LinkedHashMap<>();
            op.put("label", e.getNome());
            op.put("valor", e.getId());
            opcoes.add(op);
        }

        return ResultadoEtapa.aguardandoInput(Map.of(
            "campoPedido", "Escolha um cavaleiro",
            "salvar_em", salvarEm,
            "opcoes", opcoes
        ));
    }
}
