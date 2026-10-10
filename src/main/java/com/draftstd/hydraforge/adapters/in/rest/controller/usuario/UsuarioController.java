package com.draftstd.hydraforge.adapters.in.rest.controller.usuario;

import com.draftstd.hydraforge.adapters.in.dto.usuario.UsuarioResponseDTO;
import com.draftstd.hydraforge.ports.in.usuario.ObterUsuarioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final ObterUsuarioUseCase obterUsuarioUseCase;

    public UsuarioController(ObterUsuarioUseCase obterUsuarioUseCase) {
        this.obterUsuarioUseCase = obterUsuarioUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obterPorId(@PathVariable UUID id) {
        var response = obterUsuarioUseCase.obterPorId(id);
        return ResponseEntity.ok(new UsuarioResponseDTO(response.id(), response.nome(), response.email()));
    }
}