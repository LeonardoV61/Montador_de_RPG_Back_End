package com.draftstd.hydraforge.adapters.in.rest.controller.usuario;

import com.draftstd.hydraforge.adapters.in.dto.usuario.UsuarioResponseDTO;
import com.draftstd.hydraforge.application.response.usuario.ObterUsuarioQuery;
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
        var query = new ObterUsuarioQuery(id);
        var response = obterUsuarioUseCase.obterPorId(query);
        return ResponseEntity.ok(new UsuarioResponseDTO(
            response.id(),
            response.email(),
            response.apelido(),
            response.urlImagem(),
            response.eAdmin(),
            response.provider(),
            response.criadoEm()
        ));
    }
}