package com.draftstd.hydraforge.adapters.in.rest.controller.sessao;

import com.draftstd.hydraforge.adapters.in.dto.sessao.CenaCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.sessao.CenaUpdateDTO;
import com.draftstd.hydraforge.ports.in.command.sessao.AtualizarCenaCommand;
import com.draftstd.hydraforge.ports.in.command.sessao.CriarCenaCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.CenaQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterCenaQuery;
import com.draftstd.hydraforge.ports.in.sessao.CriarCenaUseCase;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarCenaUseCase;
import com.draftstd.hydraforge.ports.in.sessao.ObterCenaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cenas")
public class CenaController {

    private final CriarCenaUseCase criarCenaUseCase;
    private final GerenciarCenaUseCase gerenciarCenaUseCase;
    private final ObterCenaUseCase obterCenaUseCase;

    public CenaController(CriarCenaUseCase criarCenaUseCase, GerenciarCenaUseCase gerenciarCenaUseCase, ObterCenaUseCase obterCenaUseCase) {
        this.criarCenaUseCase = criarCenaUseCase;
        this.gerenciarCenaUseCase = gerenciarCenaUseCase;
        this.obterCenaUseCase = obterCenaUseCase;
    }

    @PostMapping
    public ResponseEntity<CenaQueryResponse> criar(@Valid @RequestBody CenaCreateDTO dto) {
        CriarCenaCommand command = new CriarCenaCommand(dto.sessaoId(), dto.titulo(), dto.ambiente(), dto.mapaUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(criarCenaUseCase.executar(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CenaQueryResponse> atualizar(@PathVariable UUID id, @Valid @RequestBody CenaUpdateDTO dto) {
        AtualizarCenaCommand command = new AtualizarCenaCommand(id, dto.titulo(), dto.ambiente(), dto.mapaUrl(), dto.ativa());
        return ResponseEntity.ok(gerenciarCenaUseCase.atualizar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CenaQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterCenaQuery query = new ObterCenaQuery(id);
        return ResponseEntity.ok(obterCenaUseCase.obterPorId(query));
    }
}