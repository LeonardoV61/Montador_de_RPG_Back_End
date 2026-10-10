package com.draftstd.hydraforge.adapters.in.rest.controller.entidade;

import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeInstanciaCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.entidade.EntidadeInstanciaUpadteDTO;
import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.EntidadeInstanciaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sistema.ObterEntidadeInstanciaQuery;
import com.draftstd.hydraforge.ports.in.sistema.entidade.GerenciarEntidadeInstanciaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/entidades-instancia")
public class EntidadeInstanciaController {

    private final GerenciarEntidadeInstanciaUseCase gerenciarEntidadeInstanciaUseCase;

    public EntidadeInstanciaController(GerenciarEntidadeInstanciaUseCase gerenciarEntidadeInstanciaUseCase) {
        this.gerenciarEntidadeInstanciaUseCase = gerenciarEntidadeInstanciaUseCase;
    }

    @PostMapping
    public ResponseEntity<EntidadeInstanciaQueryResponse> criar(@Valid @RequestBody EntidadeInstanciaCreateDTO dto,
                                                                @RequestHeader("X-Usuario-Id") UUID donoId) {
        CriarEntidadeInstanciaCommand command = new CriarEntidadeInstanciaCommand(
                dto.entidadeSistemaId(),
                donoId,
                dto.nome(),
                dto.valoresAtributosJson()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenciarEntidadeInstanciaUseCase.criar(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntidadeInstanciaQueryResponse> atualizar(@PathVariable UUID id,
                                                                    @Valid @RequestBody EntidadeInstanciaUpadteDTO dto) {
        AtualizarEntidadeInstanciaCommand command = new AtualizarEntidadeInstanciaCommand(
                id,
                dto.nome(),
                dto.valoresAtributosJson()
        );
        return ResponseEntity.ok(gerenciarEntidadeInstanciaUseCase.atualizar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntidadeInstanciaQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterEntidadeInstanciaQuery query = new ObterEntidadeInstanciaQuery(id);
        return ResponseEntity.ok(gerenciarEntidadeInstanciaUseCase.obterPorId(query));
    }
}