package com.draftstd.hydraforge.adapters.in.rest.controller.sistema;

import com.draftstd.hydraforge.adapters.in.dto.sistema.entidade.EntidadeInstanciaCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.sistema.entidade.EntidadeInstanciaUpdateDTO;
import com.draftstd.hydraforge.application.request.sistema.AtualizarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.application.request.sistema.CriarEntidadeInstanciaCommand;
import com.draftstd.hydraforge.application.response.sistema.EntidadeInstanciaQueryResponse;
import com.draftstd.hydraforge.application.response.sistema.ObterEntidadeInstanciaQuery;
import com.draftstd.hydraforge.ports.in.sistema.GerenciarEntidadeInstanciaUseCase;
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
                dto.valoresAtributos()
        );
        UUID novoId = gerenciarEntidadeInstanciaUseCase.criar(command);
        EntidadeInstanciaQueryResponse response = gerenciarEntidadeInstanciaUseCase.obterPorId(new ObterEntidadeInstanciaQuery(novoId));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntidadeInstanciaQueryResponse> atualizar(@PathVariable UUID id, 
                                                                   @Valid @RequestBody EntidadeInstanciaUpdateDTO dto) {
        AtualizarEntidadeInstanciaCommand command = new AtualizarEntidadeInstanciaCommand(
                id, 
                dto.nome(), 
                dto.valoresAtributos()
        );
        gerenciarEntidadeInstanciaUseCase.atualizar(command);
        EntidadeInstanciaQueryResponse response = gerenciarEntidadeInstanciaUseCase.obterPorId(new ObterEntidadeInstanciaQuery(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntidadeInstanciaQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterEntidadeInstanciaQuery query = new ObterEntidadeInstanciaQuery(id);
        return ResponseEntity.ok(gerenciarEntidadeInstanciaUseCase.obterPorId(query));
    }
}