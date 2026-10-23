package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.*;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EscopoInstancias;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ChamarProcedimentoHandler implements EtapaHandler {

    private final SessaoContexto sessaoCtx;
    private final ProcedimentoLoader loader;

    public ChamarProcedimentoHandler(SessaoContexto sessaoCtx, ProcedimentoLoader loader) {
        this.sessaoCtx = sessaoCtx;
        this.loader = loader;
    }

    @Override
    public String tipoEtapa() { return "CHAMAR_PROCEDIMENTO"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        if (!(ctx instanceof ProcedimentoContexto procedimentoCtx)) {
            return ResultadoEtapa.erro("CHAMAR_PROCEDIMENTO só pode ser usado dentro de um procedimento");
        }

        Map<String, Object> params = etapa.getParametrosEtapa();

        Object rawIdProc = params.get("id_procedimento");
        if (rawIdProc == null) {
            throw new JsonFieldNotFoundException("id_procedimento", etapa.getNome());
        }
        UUID idFilho = rawIdProc instanceof UUID u ? u : UUID.fromString(rawIdProc.toString());

        String salvarEm = (String) params.get("salvar_em");
        String escopoTipo = (String) params.getOrDefault("escopo", "HERDAR");

        EscopoInstancias escopoFilho = resolverEscopo(escopoTipo, params, procedimentoCtx);

        List<String> passarContexto;
        Object raw = params.get("passar_contexto");
        if (raw == null) {
            passarContexto = List.of();
        } else if (raw instanceof List<?>) {
            passarContexto = ((List<?>) raw).stream()
                    .map(Object::toString)
                    .toList();
        } else {
            passarContexto = List.of(raw.toString());
        }

        Map<String, Object> ctxFilho = procedimentoCtx.getContexto().copyKeys(passarContexto);

        ProcedimentoContexto filho = loader.carregar(
                LoadRequest.filho(idFilho, procedimentoCtx, escopoFilho, salvarEm, ctxFilho)
        );

        sessaoCtx.empurrar(procedimentoCtx.getIdSessao(), filho);

        return ResultadoEtapa.subProcedimentoIniciado(filho.getProcedimento().getNome());
    }

    private EscopoInstancias resolverEscopo(String tipo,
                                            Map<String, Object> params,
                                            ProcedimentoContexto pai) {
        return switch (tipo) {
            case "HERDAR"       -> pai.getEscopo();
            case "NENHUMA"      -> EscopoInstancias.nenhuma();
            case "UNICA" -> {
                String chave = (String) params.get("escopo_contexto_chave");
                Object rawId = pai.getContexto().get(chave, Object.class).orElseThrow(
                        () -> new JsonFieldNotFoundException("escopo_contexto_chave", "CHAMAR_PROCEDIMENTO")
                );
                UUID id = rawId instanceof UUID u ? u : UUID.fromString(rawId.toString());
                yield EscopoInstancias.unica(id);
            }
            case "CONTEXTO_IDS" -> {
                String chave = (String) params.get("escopo_contexto_chave");
                List<UUID> ids = pai.getContexto().getListaIds(chave);
                yield EscopoInstancias.multiplas(ids != null ? ids : List.of());
            }
            case "TODOS" -> {
                List<UUID> ids = pai.getParticipantes().stream()
                        .map(Personagem::getId)
                        .toList();
                yield EscopoInstancias.multiplas(ids);
            }
            default -> throw new IllegalArgumentException(
                    "Tipo de escopo desconhecido: " + tipo);
        };
    }
}