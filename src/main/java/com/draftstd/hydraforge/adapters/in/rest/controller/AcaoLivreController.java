package com.draftstd.hydraforge.adapters.in.rest.controller;

import com.draftstd.hydraforge.application.request.sistema.ExecutarProcedimentoCommand;
import com.draftstd.hydraforge.ports.in.sistema.GerenciarProcedimentoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/acoes-livres")
public class AcaoLivreController {

    private final GerenciarProcedimentoUseCase gerenciarProcedimentoUseCase;

    public AcaoLivreController(GerenciarProcedimentoUseCase gerenciarProcedimentoUseCase) {
        this.gerenciarProcedimentoUseCase = gerenciarProcedimentoUseCase;
    }

    @PostMapping("/executar")
    public ResponseEntity<Object> executarAcaoLivre(@Valid @RequestBody ExecutarProcedimentoCommand command) {
        Object resultado = gerenciarProcedimentoUseCase.executarProcedimento(command);
        return ResponseEntity.ok(resultado);
    }
}