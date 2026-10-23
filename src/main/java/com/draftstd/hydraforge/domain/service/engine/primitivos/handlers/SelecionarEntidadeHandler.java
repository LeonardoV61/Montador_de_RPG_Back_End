package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeSistema;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeSistemaOutputPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SelecionarEntidadeHandler implements EtapaHandler {

    private final EntidadeSistemaOutputPort entidadeSistemaPort;

    @Override
    public String tipoEtapa() { return "SELECIONAR_ENTIDADE"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();
        
        String tipo = (String) params.get("tipo_entidade");
        String salvarEm = (String) params.get("salvar_em");
        String modoEsperado = (String) params.get("modo_esperado");

        if (modoEsperado != null) {
            String modoAtual = ctx.getContexto().getStringOrThrow("modo_escolha_cavaleiro");
            if (!modoEsperado.equals(modoAtual)) {
                return ResultadoEtapa.concluida(Map.of("pulado", true));
            }
        }

        if (ctx.getContexto().containsKey(salvarEm)) {
            Object valorSalvo = ctx.getContexto().get(salvarEm, Object.class).orElseThrow();
            UUID id = valorSalvo instanceof UUID u ? u : UUID.fromString(valorSalvo.toString());
            return ResultadoEtapa.concluida(Map.of(salvarEm, id));
        }

        List<EntidadeSistema> entidades = entidadeSistemaPort.listarPorSistemaId(ctx.getIdSistema()).stream()
                .filter(e -> tipo == null || tipo.equalsIgnoreCase(e.getTipo()))
                .toList();

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