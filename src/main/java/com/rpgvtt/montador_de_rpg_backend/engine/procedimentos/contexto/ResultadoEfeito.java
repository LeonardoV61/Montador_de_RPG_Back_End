package com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto;

import java.util.List;

/**
 * Resultado da aplicação (ou expiração) de um único EfeitoAtivo durante
 * o processamento de turno/rodada. A lista de resultados reaproveita
 * ResultadoEtapa em vez de um tipo "ResultadoPrimitivo" separado —
 * desde que primitivos passaram a ser apenas handlers como qualquer
 * outro tipo_etapa, não há motivo para dois tipos de resultado distintos.
 */
public record ResultadoEfeito(
        Long idEfeitoAtivo,
        boolean expirou,
        List<ResultadoEtapa> resultados
) {
    public static ResultadoEfeito aplicado(Long idEfeitoAtivo, List<ResultadoEtapa> resultados) {
        return new ResultadoEfeito(idEfeitoAtivo, false, resultados);
    }
    public static ResultadoEfeito expirado(Long idEfeitoAtivo, List<ResultadoEtapa> resultadosExpiracao) {
        return new ResultadoEfeito(idEfeitoAtivo, true, resultadosExpiracao);
    }
}