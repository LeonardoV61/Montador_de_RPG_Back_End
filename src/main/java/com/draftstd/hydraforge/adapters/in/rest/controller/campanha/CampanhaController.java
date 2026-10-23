package com.draftstd.hydraforge.adapters.in.rest.controller.campanha;

import com.draftstd.hydraforge.adapters.in.dto.campanha.AdicionarJogadorDTO;
import com.draftstd.hydraforge.adapters.in.dto.campanha.CampanhaCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.campanha.CampanhaPapelDTO;
import com.draftstd.hydraforge.adapters.in.dto.campanha.CampanhaUpdateDTO;
import com.draftstd.hydraforge.application.request.campanha.AdicionarJogadorCampanhaCommand;
import com.draftstd.hydraforge.application.request.campanha.AlterarPapelJogadorCommand;
import com.draftstd.hydraforge.application.request.campanha.AtualizarCampanhaCommand;
import com.draftstd.hydraforge.application.request.campanha.CriarCampanhaCommand;
import com.draftstd.hydraforge.application.request.campanha.RemoverJogadorCampanhaCommand;
import com.draftstd.hydraforge.application.response.campanha.CampanhaQueryResponse;
import com.draftstd.hydraforge.application.response.campanha.ListarCampanhasPorUsuarioQuery;
import com.draftstd.hydraforge.application.response.campanha.ObterCampanhaQuery;
import com.draftstd.hydraforge.ports.in.campanha.AtualizarCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.campanha.CriarCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.campanha.GerenciarParticipantesCampanhaUseCase;
import com.draftstd.hydraforge.ports.in.campanha.ObterCampanhaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/campanhas")
public class CampanhaController {

    private final CriarCampanhaUseCase criarCampanhaUseCase;
    private final ObterCampanhaUseCase obterCampanhaUseCase;
    private final AtualizarCampanhaUseCase atualizarCampanhaUseCase;
    private final GerenciarParticipantesCampanhaUseCase gerenciarParticipantesUseCase;

    public CampanhaController(
            CriarCampanhaUseCase criarCampanhaUseCase,
            ObterCampanhaUseCase obterCampanhaUseCase,
            AtualizarCampanhaUseCase atualizarCampanhaUseCase,
            GerenciarParticipantesCampanhaUseCase gerenciarParticipantesUseCase
    ) {
        this.criarCampanhaUseCase = criarCampanhaUseCase;
        this.obterCampanhaUseCase = obterCampanhaUseCase;
        this.atualizarCampanhaUseCase = atualizarCampanhaUseCase;
        this.gerenciarParticipantesUseCase = gerenciarParticipantesUseCase;
    }

    @PostMapping
    public ResponseEntity<UUID> criar(@RequestBody @Valid CampanhaCreateDTO dto) {
        CriarCampanhaCommand command = new CriarCampanhaCommand(
                dto.nome(),
                dto.descricao(),
                dto.urlImagem(),
                dto.sistemaId(),
                dto.criadorId()
        );

        UUID campanhaId = criarCampanhaUseCase.executar(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(campanhaId)
                .toUri();

        return ResponseEntity.created(location).body(campanhaId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampanhaQueryResponse> obterPorId(@PathVariable UUID id) {
        CampanhaQueryResponse response = obterCampanhaUseCase.obterPorId(new ObterCampanhaQuery(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CampanhaQueryResponse>> listarPorUsuario(@PathVariable UUID usuarioId) {
        List<CampanhaQueryResponse> responses = obterCampanhaUseCase.listarPorUsuario(new ListarCampanhasPorUsuarioQuery(usuarioId));
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid CampanhaUpdateDTO dto
    ) {
        AtualizarCampanhaCommand command = new AtualizarCampanhaCommand(
                id,
                dto.nome(),
                dto.descricao(),
                dto.urlImagem(),
                dto.status()
        );

        atualizarCampanhaUseCase.executar(command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{campanhaId}/participantes")
    public ResponseEntity<Void> adicionarJogador(
            @PathVariable UUID campanhaId,
            @RequestBody @Valid AdicionarJogadorDTO dto
    ) {
        AdicionarJogadorCampanhaCommand command = new AdicionarJogadorCampanhaCommand(
                campanhaId,
                dto.usuarioId(),
                dto.papel(),
                dto.statusMembro()
        );

        gerenciarParticipantesUseCase.adicionarJogador(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{campanhaId}/participantes/{usuarioId}")
    public ResponseEntity<Void> alterarPapel(
            @PathVariable UUID campanhaId,
            @PathVariable UUID usuarioId,
            @RequestBody @Valid CampanhaPapelDTO dto
    ) {
        AlterarPapelJogadorCommand command = new AlterarPapelJogadorCommand(
                campanhaId,
                usuarioId,
                dto.novoPapel(),
                dto.novoStatus()
        );

        gerenciarParticipantesUseCase.alterarPapel(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{campanhaId}/participantes/{usuarioId}")
    public ResponseEntity<Void> removerJogador(
            @PathVariable UUID campanhaId,
            @PathVariable UUID usuarioId
    ) {
        RemoverJogadorCampanhaCommand command = new RemoverJogadorCampanhaCommand(campanhaId, usuarioId);
        gerenciarParticipantesUseCase.removerJogador(command);
        return ResponseEntity.noContent().build();
    }
}