package com.rpgvtt.montador_de_rpg_backend.controller.sistema;

import com.rpgvtt.montador_de_rpg_backend.dto.sistema.ProcedimentoContextoDTO;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.ProcedimentoEngine;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ProcedimentoContexto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoEngine engine;

    /**
     * Inicia um procedimento já persistido com um escopo de instância único.
     * Útil para criar personagens a partir do procedimento do sistema.
     */
    @PostMapping("/{id}/iniciar-com-instancia")
    @ResponseStatus(HttpStatus.CREATED)
    public ProcedimentoContextoDTO iniciarComInstancia(@PathVariable Long id,
                                                       @RequestParam @NotNull Long idSessao,
                                                       @RequestParam @NotNull Long idInstancia) {

        ProcedimentoContexto ctx = engine.iniciarComInstancia(id, idSessao, idInstancia);
        return ProcedimentoContextoDTO.from(ctx);
    }

    /**
     * Inicia um procedimento sem instância (configurações, criação global etc.)
     */
    @PostMapping("/{id}/iniciar-sem-instancia")
    @ResponseStatus(HttpStatus.CREATED)
    public ProcedimentoContextoDTO iniciarSemInstancia(@PathVariable Long id,
                                                       @RequestParam @NotNull Long idSessao) {

        ProcedimentoContexto ctx = engine.iniciarSemInstancia(id, idSessao);
        return ProcedimentoContextoDTO.from(ctx);
    }

    /**
     * Inicia um procedimento com múltiplas instâncias (ex: todos os participantes)
     */
    @PostMapping("/{id}/iniciar-com-multiplas")
    @ResponseStatus(HttpStatus.CREATED)
    public ProcedimentoContextoDTO iniciarComMultiplas(@PathVariable Long id,
                                                       @RequestParam @NotNull Long idSessao,
                                                       @RequestBody java.util.List<Long> ids) {

        ProcedimentoContexto ctx = engine.iniciarComMultiplos(id, idSessao, ids);
        return ProcedimentoContextoDTO.from(ctx);
    }
}