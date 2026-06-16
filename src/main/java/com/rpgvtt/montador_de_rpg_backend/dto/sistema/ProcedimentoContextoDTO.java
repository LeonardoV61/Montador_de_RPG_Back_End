package com.rpgvtt.montador_de_rpg_backend.dto.sistema;

import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ProcedimentoContexto;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ProcedimentoContexto.Status;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ResultadoEtapa;
import lombok.Value;

import java.util.List;
import java.util.Objects;

@Value
public class ProcedimentoContextoDTO {

    Status status;
    String nomeProcedimento;
    int etapaAtual;
    
    Object inputSolicitado;

    List<Object> resultadosCiclo;
    String erro;

    public static ProcedimentoContextoDTO from(ProcedimentoContexto ctx) {
        if (ctx == null) {
            return new ProcedimentoContextoDTO(Status.CONCLUIDO, null, 0, null, List.of(), null);
        }

        // Collect dados from every ResultadoEtapa produced in this advance cycle.
        // "This cycle" = everything added to historico since the last AGUARDANDO_INPUT.
        // We track this by finding entries after the last null-dados entry.
        List<Object> resultadosCiclo = ctx.getHistorico().stream()
                .map(ResultadoEtapa::dados)
                .filter(Objects::nonNull)
                .toList();

        // The pending input request is the dados of the last AGUARDANDO_INPUT entry
        Object inputSolicitado = null;
        if (ctx.getStatus() == Status.CONCLUIDO && ctx.getEtapaPendente() != null) {
            inputSolicitado = ctx.getHistorico().stream()
                    .filter(r -> r.tipo() == ResultadoEtapa.TipoResultado.AGUARDANDO_INPUT)
                    .reduce((first, second) -> second) // last one
                    .map(ResultadoEtapa::dados)
                    .orElse(null);
        }

        return new ProcedimentoContextoDTO(
                ctx.getStatus(),
                ctx.getProcedimento().getNome(),
                ctx.getEtapaAtual(),
                inputSolicitado,
                resultadosCiclo,
                ctx.getStatus() == Status.ERRO
                        ? ctx.getHistorico().getLast().mensagem()
                        : null
        );
    }
}