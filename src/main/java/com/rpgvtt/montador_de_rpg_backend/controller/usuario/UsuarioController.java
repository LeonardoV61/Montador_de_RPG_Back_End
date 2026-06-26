package com.rpgvtt.montador_de_rpg_backend.controller.usuario;

import com.rpgvtt.montador_de_rpg_backend.dto.usuario.UsuarioResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.usuario.UsuarioUpdateDTO;
import com.rpgvtt.montador_de_rpg_backend.service.usuario.UsuarioService;
import jakarta.validation.Valid;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.rpgvtt.montador_de_rpg_backend.security.UsuarioPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        UsuarioResponseDTO dto = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@PathVariable String email) {
        UsuarioResponseDTO dto = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> lista = usuarioService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id,
                                                        @RequestBody @Valid UsuarioUpdateDTO dto) {
        UsuarioResponseDTO atualizado = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> perfil(@AuthenticationPrincipal UsuarioPrincipal principal) {
        UsuarioResponseDTO dto = usuarioService.buscarPorId(principal.getId());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/id-por-email/{email}")
    public ResponseEntity<Long> buscarIdPorEmail(@PathVariable String email) {
        Long id = usuarioService.buscarIdPorEmail(email);
        return ResponseEntity.ok(id);
    }
}