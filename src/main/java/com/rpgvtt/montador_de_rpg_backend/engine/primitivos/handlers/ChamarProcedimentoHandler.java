package com.rpgvtt.montador_de_rpg_backend.engine.primitivos.handlers;

import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.EtapaProcedimento;
import com.rpgvtt.montador_de_rpg_backend.engine.exceptions.JsonFieldNotFoundException;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.*;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EscopoInstancias;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EtapaExecutavel;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EtapaHandler;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.ExecucaoContexto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChamarProcedimentoHandler implements EtapaHandler {

    private final SessaoContexto sessaoCtx;
    private final ProcedimentoLoader loader;
    private final ObjectMapper mapper;

    @Override
    public String tipoEtapa() { return "CHAMAR_PROCEDIMENTO"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        if (!(ctx instanceof ProcedimentoContexto procedimentoCtx)) {
            return ResultadoEtapa.erro("CHAMAR_PROCEDIMENTO só pode ser usado dentro de um procedimento");
        }
        return executar((EtapaProcedimento) etapa, procedimentoCtx);
    }

    public ResultadoEtapa executar(EtapaProcedimento etapa,
                                   ProcedimentoContexto procedimentoCtx) {

        Map<String, Object> params = mapper.convertValue(etapa.getParametrosEtapa(), new TypeReference<>() {});

        Long idFilho  = ((Number) params.get("id_procedimento")).longValue();
        String salvarEm  = (String) params.get("salvar_em");
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

        // Signal to the engine: don't advance parent yet, execute child first
        return ResultadoEtapa.subProcedimentoIniciado(filho.getProcedimento().getNome());
    }

    /**
     * Resolves which instances the child procedure operates on.
     * "HERDAR"       → same scope as parent (most common for single-actor chains)
     * "NENHUMA"      → child needs no instance (setup/teardown procedures)
     * "UNICA"        → a specific instance, read from contexto key
     * "CONTEXTO_IDS" → list of IDs previously saved in parent contexto
     * "TODOS"        → all session participants
     */

    private EscopoInstancias resolverEscopo(String tipo,
                                            Map<String, Object> params,
                                            ProcedimentoContexto pai) {
        return switch (tipo) {
            case "HERDAR"       -> pai.getEscopo();
            case "NENHUMA"      -> EscopoInstancias.nenhuma();
            case "UNICA" -> {
                String chave = (String) params.get("escopo_contexto_chave");
                Long id   = pai.getContexto().getLong(chave).orElseThrow(
                        () -> new JsonFieldNotFoundException("escopo_contexto_chave", "CHAMAR_PROCEDIMENTO")
                );
                yield EscopoInstancias.unica(id);
            }
            case "CONTEXTO_IDS" -> {
                String chave = (String) params.get("escopo_contexto_chave");
                List<Long> ids = pai.getContexto().getListaIds(chave);
                yield EscopoInstancias.multiplas(ids != null ? ids : List.of());
            }
            case "TODOS" -> {
                List<Long> ids = pai.getParticipantes().stream()
                        .map(p -> p.getInstancia().getId())
                        .toList();
                yield EscopoInstancias.multiplas(ids);
            }
            default -> throw new IllegalArgumentException(
                    "Tipo de escopo desconhecido: " + tipo);
        };
    }
}
