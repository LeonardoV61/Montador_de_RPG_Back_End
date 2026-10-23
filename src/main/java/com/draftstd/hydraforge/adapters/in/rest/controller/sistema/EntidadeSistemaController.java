package com.draftstd.hydraforge.adapters.in.rest.controller.sistema;

import com.draftstd.hydraforge.adapters.in.dto.sistema.entidade.EntidadeSistemaCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.sistema.entidade.EntidadeSistemaUpdateDTO;
import com.draftstd.hydraforge.application.request.sistema.AtualizarEntidadeSistemaCommand;
import com.draftstd.hydraforge.application.request.sistema.CriarEntidadeSistemaCommand;
import com.draftstd.hydraforge.application.response.sistema.EntidadeSistemaQueryResponse;
import com.draftstd.hydraforge.application.response.sistema.ObterEntidadeSistemaQuery;
import com.draftstd.hydraforge.ports.in.sistema.GerenciarEntidadeSistemaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/entidades-sistema")
public class EntidadeSistemaController {

    private final GerenciarEntidadeSistemaUseCase gerenciarEntidadeSistemaUseCase;

    public EntidadeSistemaController(GerenciarEntidadeSistemaUseCase gerenciarEntidadeSistemaUseCase) {
        this.gerenciarEntidadeSistemaUseCase = gerenciarEntidadeSistemaUseCase;
    }

    @PostMapping
    public ResponseEntity<EntidadeSistemaQueryResponse> criar(@Valid @RequestBody EntidadeSistemaCreateDTO dto) {
        CriarEntidadeSistemaCommand command = new CriarEntidadeSistemaCommand(
                dto.sistemaId(), 
                dto.nome(), 
                dto.tipo(), 
                dto.schema()
        );
        UUID novoId = gerenciarEntidadeSistemaUseCase.criar(command);
        EntidadeSistemaQueryResponse response = gerenciarEntidadeSistemaUseCase.obterPorId(new ObterEntidadeSistemaQuery(novoId));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntidadeSistemaQueryResponse> atualizar(@PathVariable UUID id,
                                                                   @Valid @RequestBody EntidadeSistemaUpdateDTO dto) {
        AtualizarEntidadeSistemaCommand command = new AtualizarEntidadeSistemaCommand(
                id, 
                dto.nome(), 
                dto.tipo(), 
                dto.schema()
        );
        gerenciarEntidadeSistemaUseCase.atualizar(command);
        EntidadeSistemaQueryResponse response = gerenciarEntidadeSistemaUseCase.obterPorId(new ObterEntidadeSistemaQuery(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntidadeSistemaQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterEntidadeSistemaQuery query = new ObterEntidadeSistemaQuery(id);
        return ResponseEntity.ok(gerenciarEntidadeSistemaUseCase.obterPorId(query));
    }
}