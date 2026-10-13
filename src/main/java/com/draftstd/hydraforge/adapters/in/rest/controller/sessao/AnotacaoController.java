package com.draftstd.hydraforge.adapters.in.rest.controller.sessao;

import com.draftstd.hydraforge.adapters.in.dto.sessao.AnotacaoCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.sessao.AnotacaoUpdateDTO;
import com.draftstd.hydraforge.ports.in.command.sessao.AtualizarAnotacaoCommand;
import com.draftstd.hydraforge.ports.in.command.sessao.CriarAnotacaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterAnotacaoQuery;
import com.draftstd.hydraforge.ports.in.sessao.CriarAnotacaoUseCase;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarAnotacaoUseCase;
import com.draftstd.hydraforge.ports.in.sessao.ObterAnotacaoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/anotacoes")
public class AnotacaoController {

    private final CriarAnotacaoUseCase criarAnotacaoUseCase;
    private final GerenciarAnotacaoUseCase gerenciarAnotacaoUseCase;
    private final ObterAnotacaoUseCase obterAnotacaoUseCase;

    public AnotacaoController(CriarAnotacaoUseCase criarAnotacaoUseCase, GerenciarAnotacaoUseCase gerenciarAnotacaoUseCase, ObterAnotacaoUseCase obterAnotacaoUseCase) {
        this.criarAnotacaoUseCase = criarAnotacaoUseCase;
        this.gerenciarAnotacaoUseCase = gerenciarAnotacaoUseCase;
        this.obterAnotacaoUseCase = obterAnotacaoUseCase;
    }

    @PostMapping
    public ResponseEntity<AnotacaoQueryResponse> criar(@Valid @RequestBody AnotacaoCreateDTO dto, @RequestHeader("X-Usuario-Id") UUID autorId) {
        CriarAnotacaoCommand command = new CriarAnotacaoCommand(dto.sessaoId(), autorId, dto.titulo(), dto.conteudo(), dto.privada());
        return ResponseEntity.status(HttpStatus.CREATED).body(criarAnotacaoUseCase.executar(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnotacaoQueryResponse> atualizar(@PathVariable UUID id, @Valid @RequestBody AnotacaoUpdateDTO dto) {
        AtualizarAnotacaoCommand command = new AtualizarAnotacaoCommand(id, dto.titulo(), dto.conteudo(), dto.privada());
        return ResponseEntity.ok(gerenciarAnotacaoUseCase.atualizar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnotacaoQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterAnotacaoQuery query = new ObterAnotacaoQuery(id);
        return ResponseEntity.ok(obterAnotacaoUseCase.obterPorId(query));
    }
}