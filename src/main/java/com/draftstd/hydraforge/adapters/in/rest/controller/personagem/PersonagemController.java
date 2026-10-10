package com.draftstd.hydraforge.adapters.in.rest.controller.personagem;

import com.draftstd.hydraforge.adapters.in.dto.personagem.*;
import com.draftstd.hydraforge.ports.in.command.personagem.*;
import com.draftstd.hydraforge.ports.in.personagem.*;
import com.draftstd.hydraforge.ports.in.query.personagem.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/personagens")
public class PersonagemController {

    private final CriarPersonagemUseCase criarPersonagemUseCase;
    private final AtualizarPersonagemUseCase atualizarPersonagemUseCase;
    private final DeletarPersonagemUseCase deletarPersonagemUseCase;
    private final ObterPersonagemUseCase obterPersonagemUseCase;
    private final RealizarDescansoUseCase realizarDescansoUseCase;
    private final UsarItemConsumivelUseCase usarItemConsumivelUseCase;

    public PersonagemController(CriarPersonagemUseCase criarPersonagemUseCase,
                                AtualizarPersonagemUseCase atualizarPersonagemUseCase,
                                DeletarPersonagemUseCase deletarPersonagemUseCase,
                                ObterPersonagemUseCase obterPersonagemUseCase,
                                RealizarDescansoUseCase realizarDescansoUseCase,
                                UsarItemConsumivelUseCase usarItemConsumivelUseCase) {
        this.criarPersonagemUseCase = criarPersonagemUseCase;
        this.atualizarPersonagemUseCase = atualizarPersonagemUseCase;
        this.deletarPersonagemUseCase = deletarPersonagemUseCase;
        this.obterPersonagemUseCase = obterPersonagemUseCase;
        this.realizarDescansoUseCase = realizarDescansoUseCase;
        this.usarItemConsumivelUseCase = usarItemConsumivelUseCase;
    }

    @PostMapping
    public ResponseEntity<PersonagemQueryResponse> criar(@Valid @RequestBody PersonagemCreateDTO dto,
                                                          @RequestHeader("X-Usuario-Id") UUID usuarioId) {
        CriarPersonagemCommand command = new CriarPersonagemCommand(
                usuarioId,
                dto.sistemaId(),
                dto.campanhaId(),
                dto.nome(),
                dto.conceito(),
                dto.dadosJson()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(criarPersonagemUseCase.executar(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonagemQueryResponse> atualizar(@PathVariable UUID id,
                                                             @Valid @RequestBody PersonagemUpdateDTO dto) {
        AtualizarPersonagemCommand command = new AtualizarPersonagemCommand(
                id,
                dto.nome(),
                dto.conceito(),
                dto.dadosJson()
        );
        return ResponseEntity.ok(atualizarPersonagemUseCase.executar(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id,
                                        @RequestHeader("X-Usuario-Id") UUID usuarioId) {
        DeletarPersonagemCommand command = new DeletarPersonagemCommand(id, usuarioId);
        deletarPersonagemUseCase.executar(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonagemQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterPersonagemQuery query = new ObterPersonagemQuery(id);
        return ResponseEntity.ok(obterPersonagemUseCase.obterPorId(query));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PersonagemQueryResponse>> listarPorUsuario(@PathVariable UUID usuarioId) {
        ListarPersonagensPorUsuarioQuery query = new ListarPersonagensPorUsuarioQuery(usuarioId);
        return ResponseEntity.ok(obterPersonagemUseCase.listarPorUsuario(query));
    }

    @PostMapping("/{id}/descanso")
    public ResponseEntity<PersonagemQueryResponse> descansar(@PathVariable UUID id,
                                                             @RequestParam String tipoDescanso) {
        RealizarDescansoCommand command = new RealizarDescansoCommand(id, tipoDescanso);
        return ResponseEntity.ok(realizarDescansoUseCase.executar(command));
    }

    @PostMapping("/{id}/usar-item")
    public ResponseEntity<PersonagemQueryResponse> usarItem(@PathVariable UUID id,
                                                            @Valid @RequestBody UsoItemDTO dto) {
        UsarItemConsumivelCommand command = new UsarItemConsumivelCommand(
                id,
                dto.itemId(),
                dto.alvoId()
        );
        return ResponseEntity.ok(usarItemConsumivelUseCase.executar(command));
    }
}