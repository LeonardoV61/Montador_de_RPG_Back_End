package com.draftstd.hydraforge.adapters.in.rest.controller.entidade;

import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeSistemaCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeSistemaUpdateDTO;
import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarEntidadeSistemaCommand;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarEntidadeSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.EntidadeSistemaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sistema.ObterEntidadeSistemaQuery;
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
        CriarEntidadeSistemaCommand command = new CriarEntidadeSistemaCommand(dto.sistemaId(), dto.nome(), dto.tipo(), dto.schemaJson());
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenciarEntidadeSistemaUseCase.criar(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntidadeSistemaQueryResponse> atualizar(@PathVariable UUID id,
                                                                   @Valid @RequestBody EntidadeSistemaUpdateDTO dto) {
        AtualizarEntidadeSistemaCommand command = new AtualizarEntidadeSistemaCommand(id, dto.nome(), dto.tipo(), dto.schemaJson());
        return ResponseEntity.ok(gerenciarEntidadeSistemaUseCase.atualizar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntidadeSistemaQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterEntidadeSistemaQuery query = new ObterEntidadeSistemaQuery(id);
        return ResponseEntity.ok(gerenciarEntidadeSistemaUseCase.obterPorId(query));
    }
}