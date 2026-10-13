package com.draftstd.hydraforge.adapters.in.rest.controller.usuario;

import com.draftstd.hydraforge.adapters.in.dto.usuario.AmizadeResponseDTO;
import com.draftstd.hydraforge.adapters.in.dto.usuario.AmizadeSolicitarDTO;
import com.draftstd.hydraforge.ports.in.usuario.GerenciarAmizadeUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/amigos")
public class AmigoController {

    private final GerenciarAmizadeUseCase gerenciarAmizadeUseCase;

    public AmigoController(GerenciarAmizadeUseCase gerenciarAmizadeUseCase) {
        this.gerenciarAmizadeUseCase = gerenciarAmizadeUseCase;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<Void> solicitarAmizade(@Valid @RequestBody AmizadeSolicitarDTO dto, @RequestHeader("X-Usuario-Id") UUID remetenteId) {
        gerenciarAmizadeUseCase.enviarSolicitacao(remetenteId, dto.destinatarioId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{amigoId}/aceitar")
    public ResponseEntity<Void> aceitarAmizade(@PathVariable UUID amigoId, @RequestHeader("X-Usuario-Id") UUID usuarioId) {
        gerenciarAmizadeUseCase.aceitarSolicitacao(usuarioId, amigoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{amigoId}")
    public ResponseEntity<Void> removerAmizade(@PathVariable UUID amigoId, @RequestHeader("X-Usuario-Id") UUID usuarioId) {
        gerenciarAmizadeUseCase.recusarOuRemover(usuarioId, amigoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AmizadeResponseDTO>> listarAmigos(@RequestHeader("X-Usuario-Id") UUID usuarioId) {
        return ResponseEntity.ok(gerenciarAmizadeUseCase.listarAmigos(usuarioId));
    }
}