package com.draftstd.hydraforge.adapters.in.rest.controller.usuario;

import com.draftstd.hydraforge.adapters.in.dto.usuario.AmizadeResponseDTO;
import com.draftstd.hydraforge.adapters.in.dto.usuario.AmizadeSolicitarDTO;
import com.draftstd.hydraforge.application.request.usuario.EnviarSolicitacaoAmizadeCommand;
import com.draftstd.hydraforge.application.request.usuario.ResponderSolicitacaoAmizadeCommand;
import com.draftstd.hydraforge.application.response.usuario.ListarAmigosQuery;
import com.draftstd.hydraforge.domain.enums.StatusAmizade;
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
    public ResponseEntity<Void> solicitarAmizade(@Valid @RequestBody AmizadeSolicitarDTO dto, 
                                                 @RequestHeader("X-Usuario-Id") UUID remetenteId) {
        var command = new EnviarSolicitacaoAmizadeCommand(remetenteId, dto.destinatarioId());
        gerenciarAmizadeUseCase.enviarSolicitacao(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{amigoId}/aceitar")
    public ResponseEntity<Void> aceitarAmizade(@PathVariable UUID amigoId, 
                                               @RequestHeader("X-Usuario-Id") UUID usuarioId) {
        var command = new ResponderSolicitacaoAmizadeCommand(amigoId, usuarioId, StatusAmizade.ACEITO);
        gerenciarAmizadeUseCase.aceitarSolicitacao(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{amigoId}")
    public ResponseEntity<Void> removerAmizade(@PathVariable UUID amigoId, 
                                               @RequestHeader("X-Usuario-Id") UUID usuarioId) {
        var command = new ResponderSolicitacaoAmizadeCommand(amigoId, usuarioId, StatusAmizade.REJEITADO);
        gerenciarAmizadeUseCase.recusarOuRemover(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AmizadeResponseDTO>> listarAmigos(@RequestHeader("X-Usuario-Id") UUID usuarioId) {
        var query = new ListarAmigosQuery(usuarioId);
        var amigos = gerenciarAmizadeUseCase.listarAmigos(query).stream()
                .map(q -> new AmizadeResponseDTO(
                        q.idRemetente(),
                        q.idDestinatario(),
                        q.apelidoAmigo(),
                        q.urlImagemAmigo(),
                        q.status(),
                        q.criadaEm(),
                        q.aceitaEm()
                ))
                .toList();

        return ResponseEntity.ok(amigos);
    }
}