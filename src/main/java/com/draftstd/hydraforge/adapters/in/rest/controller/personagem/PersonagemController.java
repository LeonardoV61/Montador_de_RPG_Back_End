package com.draftstd.hydraforge.adapters.in.rest.controller.personagem;

import com.draftstd.hydraforge.adapters.in.dto.personagem.*;
import com.draftstd.hydraforge.application.request.personagem.*;
import com.draftstd.hydraforge.application.response.personagem.*;
import com.draftstd.hydraforge.ports.in.personagem.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
            dto.instanciaId(),
            dto.nome(),
            dto.historia(),
            dto.aparencia(),
            dto.urlImagem(),
            dto.notasJogador(),
            dto.atributos()
        );
        UUID idCriado = criarPersonagemUseCase.executar(command);
        PersonagemQueryResponse response = obterPersonagemUseCase.obterPorId(new ObterPersonagemQuery(idCriado));
        return ResponseEntity.created(URI.create("/api/v1/personagens/" + idCriado)).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable UUID id,
                                         @RequestHeader("X-Usuario-Id") UUID usuarioId,
                                         @Valid @RequestBody PersonagemUpdateDTO dto) {
        AtualizarPersonagemCommand command = new AtualizarPersonagemCommand(
            id,
            usuarioId,
            dto.nome(),
            dto.historia(),
            dto.aparencia(),
            dto.urlImagem(),
            dto.notasJogador(),
            dto.atributos(),
            dto.sessaoId()
        );
        atualizarPersonagemUseCase.executar(command);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id,
                                        @RequestHeader("X-Usuario-Id") UUID usuarioId,
                                        @RequestParam(required = false) UUID sessaoId) {
        DeletarPersonagemCommand command = new DeletarPersonagemCommand(id, usuarioId, sessaoId);
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
    public ResponseEntity<Void> descansar(@PathVariable UUID id,
                                         @RequestHeader("X-Usuario-Id") UUID usuarioId,
                                         @RequestParam String tipoDescanso,
                                         @RequestParam(required = false) UUID sessaoId) {
        RealizarDescansoCommand command = new RealizarDescansoCommand(id, usuarioId, tipoDescanso, sessaoId);
        realizarDescansoUseCase.executar(command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/usar-item")
    public ResponseEntity<Void> usarItem(@PathVariable UUID id,
                                         @RequestHeader("X-Usuario-Id") UUID usuarioId,
                                         @Valid @RequestBody UsoItemDTO dto) {
        UsarItemConsumivelCommand command = new UsarItemConsumivelCommand(id, usuarioId, dto.itemId(), dto.alvoId(), dto.sessaoId());
        usarItemConsumivelUseCase.executar(command);
        return ResponseEntity.noContent().build();
    }
}