package com.rpgvtt.montador_de_rpg_backend.engine.procedimentos;

import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.EtapaProcedimento;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ProcedimentoContexto;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ResultadoEtapa;

public interface EtapaHandler {

    String tipoEtapa();

    ResultadoEtapa executar(
            EtapaProcedimento etapa,
            ProcedimentoContexto estado
    );
}
