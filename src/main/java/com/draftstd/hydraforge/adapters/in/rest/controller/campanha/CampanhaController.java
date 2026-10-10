package com.draftstd.hydraforge.adapters.in.rest.controller.campanha;

import com.draftstd.hydraforge.adapters.in.dto.campanha.*;
import com.draftstd.hydraforge.ports.in.campanha.*;
import com.draftstd.hydraforge.ports.in.command.campanha.*;
import com.draftstd.hydraforge.ports.in.query.campanha.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/campanhas")
public class CampanhaController {

    private final CriarCampanhaUseCase criarCampanhaUseCase;
    private final AtualizarCampanhaUseCase atualizarCampanhaUseCase;
    private final ObterCampanhaUseCase obterCampanhaUseCase;
    private final GerenciarParticipantesCampanhaUseCase gerenciarParticipantesCampanhaUseCase;

    public CampanhaController(CriarCampanhaUseCase criarCampanhaUseCase,
                              AtualizarCampanhaUseCase atualizarCampanhaUseCase,
                              ObterCampanhaUseCase obterCampanhaUseCase,
                              GerenciarParticipantesCampanhaUseCase gerenciarParticipantesCampanhaUseCase) {
        this.criarCampanhaUseCase = criarCampanhaUseCase;
        this.atualizarCampanhaUseCase = atualizarCampanhaUseCase;
        this.obterCampanhaUseCase = obterCampanhaUseCase;
        this.gerenciarParticipantesCampanhaUseCase = gerenciarParticipantesCampanhaUseCase;
    }

    @PostMapping
    public ResponseEntity<CampanhaQueryResponse> criar(@Valid @RequestBody CampanhaCreateDTO dto,
                                                        @RequestHeader("X-Usuario-Id") UUID usuarioId) {
        CriarCampanhaCommand command = new CriarCampanhaCommand(
                dto.nome(),
                dto.descricao(),
                dto.sistemaId(),
                usuarioId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(criarCampanhaUseCase.executar(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampanhaQueryResponse> atualizar(@PathVariable UUID id,
                                                           @Valid @RequestBody CampanhaUpdateDTO dto) {
        AtualizarCampanhaCommand command = new AtualizarCampanhaCommand(
                id,
                dto.nome(),
                dto.descricao(),
                dto.status()
        );
        return ResponseEntity.ok(atualizarCampanhaUseCase.executar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampanhaQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterCampanhaQuery query = new ObterCampanhaQuery(id);
        return ResponseEntity.ok(obterCampanhaUseCase.obterPorId(query));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CampanhaQueryResponse>> listarPorUsuario(@PathVariable UUID usuarioId) {
        ListarCampanhasPorUsuarioQuery query = new ListarCampanhasPorUsuarioQuery(usuarioId);
        return ResponseEntity.ok(obterCampanhaUseCase.listarPorUsuario(query));
    }

    @PostMapping("/{id}/participantes")
    public ResponseEntity<CampanhaQueryResponse> adicionarJogador(@PathVariable UUID id,
                                                                   @Valid @RequestBody AdicionarJogadorDTO dto) {
        AdicionarJogadorCampanhaCommand command = new AdicionarJogadorCampanhaCommand(
                id,
                dto.usuarioId(),
                dto.papel()
        );
        return ResponseEntity.ok(gerenciarParticipantesCampanhaUseCase.adicionarJogador(command));
    }

    @DeleteMapping("/{id}/participantes/{usuarioId}")
    public ResponseEntity<CampanhaQueryResponse> removerJogador(@PathVariable UUID id,
                                                                 @PathVariable UUID usuarioId) {
        RemoverJogadorCampanhaCommand command = new RemoverJogadorCampanhaCommand(id, usuarioId);
        return ResponseEntity.ok(gerenciarParticipantesCampanhaUseCase.removerJogador(command));
    }

    @PatchMapping("/{id}/participantes/papel")
    public ResponseEntity<CampanhaQueryResponse> alterarPapel(@PathVariable UUID id,
                                                               @Valid @RequestBody CampanhaPapelDTO dto) {
        AlterarPapelJogadorCommand command = new AlterarPapelJogadorCommand(
                id,
                dto.usuarioId(),
                dto.novoPapel()
        );
        return ResponseEntity.ok(gerenciarParticipantesCampanhaUseCase.alterarPapel(command));
    }
}