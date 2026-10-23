package com.draftstd.hydraforge.adapters.in.rest.controller.sessao;

import com.draftstd.hydraforge.adapters.in.dto.sessao.AnotacaoCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.sessao.AnotacaoUpdateDTO;
import com.draftstd.hydraforge.application.request.sessao.AtualizarAnotacaoCommand;
import com.draftstd.hydraforge.application.request.sessao.CriarAnotacaoCommand;
import com.draftstd.hydraforge.application.response.sessao.AnotacaoQueryResponse;
import com.draftstd.hydraforge.application.response.sessao.ObterAnotacaoQuery;
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

    public AnotacaoController(
            CriarAnotacaoUseCase criarAnotacaoUseCase,
            GerenciarAnotacaoUseCase gerenciarAnotacaoUseCase,
            ObterAnotacaoUseCase obterAnotacaoUseCase
    ) {
        this.criarAnotacaoUseCase = criarAnotacaoUseCase;
        this.gerenciarAnotacaoUseCase = gerenciarAnotacaoUseCase;
        this.obterAnotacaoUseCase = obterAnotacaoUseCase;
    }

    @PostMapping
    public ResponseEntity<AnotacaoQueryResponse> criar(
            @Valid @RequestBody AnotacaoCreateDTO dto,
            @RequestHeader("X-Usuario-Id") UUID autorId
    ) {
        Boolean ePrivado = dto.privada() != null ? dto.privada() : false;

        CriarAnotacaoCommand command = new CriarAnotacaoCommand(
                dto.sessaoId(),
                autorId,
                dto.sessaoId(), // utiliza sessaoId como valor padrão para campanhaId
                dto.titulo(),
                dto.conteudo(),
                "GERAL",        // categoria padrão
                "{}",           // anotacaoComplexaJson padrão
                ePrivado
        );

        UUID novoId = criarAnotacaoUseCase.executar(command);

        // Busca o objeto completo para retornar o AnotacaoQueryResponse
        ObterAnotacaoQuery query = new ObterAnotacaoQuery(novoId);
        AnotacaoQueryResponse response = obterAnotacaoUseCase.obterPorId(query);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnotacaoQueryResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AnotacaoUpdateDTO dto
    ) {
        Boolean ePrivado = dto.privada() != null ? dto.privada() : false;

        AtualizarAnotacaoCommand command = new AtualizarAnotacaoCommand(
                id,
                dto.titulo() != null ? dto.titulo() : "",
                dto.conteudo() != null ? dto.conteudo() : "",
                "GERAL", // categoria padrão
                ePrivado
        );

        gerenciarAnotacaoUseCase.atualizarAnotacao(command);

        // Busca a anotação atualizada para retornar no corpo do Response
        ObterAnotacaoQuery query = new ObterAnotacaoQuery(id);
        AnotacaoQueryResponse response = obterAnotacaoUseCase.obterPorId(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnotacaoQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterAnotacaoQuery query = new ObterAnotacaoQuery(id);
        return ResponseEntity.ok(obterAnotacaoUseCase.obterPorId(query));
    }
}