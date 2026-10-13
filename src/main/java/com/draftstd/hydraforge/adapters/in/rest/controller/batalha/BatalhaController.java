package com.draftstd.hydraforge.adapters.in.rest.controller.batalha;

import com.draftstd.hydraforge.adapters.in.dto.batalha.IniciarBatalhaRequestDTO;
import com.draftstd.hydraforge.ports.in.batalha.EncerrarBatalhaUseCase;
import com.draftstd.hydraforge.ports.in.batalha.GerenciarParticipantesBatalhaUseCase;
import com.draftstd.hydraforge.ports.in.batalha.IniciarBatalhaUseCase;
import com.draftstd.hydraforge.ports.in.command.batalha.AdicionarParticipanteBatalhaCommand;
import com.draftstd.hydraforge.ports.in.command.batalha.EncerrarBatalhaCommand;
import com.draftstd.hydraforge.ports.in.command.batalha.IniciarBatalhaCommand;
import com.draftstd.hydraforge.ports.in.command.batalha.RemoverParticipanteBatalhaCommand;
import com.draftstd.hydraforge.ports.in.query.batalha.BatalhaQueryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/batalhas")
public class BatalhaController {

    private final IniciarBatalhaUseCase iniciarBatalhaUseCase;
    private final EncerrarBatalhaUseCase encerrarBatalhaUseCase;
    private final GerenciarParticipantesBatalhaUseCase gerenciarParticipantesBatalhaUseCase;

    public BatalhaController(IniciarBatalhaUseCase iniciarBatalhaUseCase, EncerrarBatalhaUseCase encerrarBatalhaUseCase, GerenciarParticipantesBatalhaUseCase gerenciarParticipantesBatalhaUseCase) {
        this.iniciarBatalhaUseCase = iniciarBatalhaUseCase;
        this.encerrarBatalhaUseCase = encerrarBatalhaUseCase;
        this.gerenciarParticipantesBatalhaUseCase = gerenciarParticipantesBatalhaUseCase;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<BatalhaQueryResponse> iniciar(@Valid @RequestBody IniciarBatalhaRequestDTO dto) {
        IniciarBatalhaCommand command = new IniciarBatalhaCommand(dto.idSessao(), dto.idCena(), dto.idsInstanciasJogadores(), dto.idsInstanciasInimigos(), dto.lados());
        return ResponseEntity.status(HttpStatus.CREATED).body(iniciarBatalhaUseCase.executar(command));
    }

    @PostMapping("/{id}/encerrar")
    public ResponseEntity<BatalhaQueryResponse> encerrar(@PathVariable UUID id, @RequestParam(defaultValue = "ENCERRADO_MANUALMENTE") String motivo) {
        EncerrarBatalhaCommand command = new EncerrarBatalhaCommand(id, motivo);
        return ResponseEntity.ok(encerrarBatalhaUseCase.executar(command));
    }

    @PostMapping("/{id}/participantes")
    public ResponseEntity<BatalhaQueryResponse> adicionarParticipante(@PathVariable UUID id, @RequestParam UUID entidadeInstanciaId, @RequestParam String tipo, @RequestParam(defaultValue = "0") Integer iniciativa) {
        AdicionarParticipanteBatalhaCommand command = new AdicionarParticipanteBatalhaCommand(id, entidadeInstanciaId, tipo, iniciativa);
        return ResponseEntity.ok(gerenciarParticipantesBatalhaUseCase.adicionarParticipante(command));
    }

    @DeleteMapping("/{id}/participantes/{entidadeInstanciaId}")
    public ResponseEntity<BatalhaQueryResponse> removerParticipante(@PathVariable UUID id, @PathVariable UUID entidadeInstanciaId) {
        RemoverParticipanteBatalhaCommand command = new RemoverParticipanteBatalhaCommand(id, entidadeInstanciaId);
        return ResponseEntity.ok(gerenciarParticipantesBatalhaUseCase.removerParticipante(command));
    }
}