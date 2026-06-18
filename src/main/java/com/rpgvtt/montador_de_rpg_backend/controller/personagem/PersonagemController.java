package com.rpgvtt.montador_de_rpg_backend.controller.personagem;

import com.rpgvtt.montador_de_rpg_backend.dto.personagem.PersonagemCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.personagem.PersonagemCompletoCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.personagem.PersonagemResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.personagem.PersonagemUpdateDTO;
import com.rpgvtt.montador_de_rpg_backend.service.personagem.PersonagemService;
import com.rpgvtt.montador_de_rpg_backend.security.UsuarioPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personagens")
public class PersonagemController {

    private final PersonagemService personagemService;

    public PersonagemController(PersonagemService personagemService) {
        this.personagemService = personagemService;
    }

    @PostMapping
    public ResponseEntity<PersonagemResponseDTO> criar(@RequestBody @Valid PersonagemCreateDTO dto) {
        PersonagemResponseDTO novoPersonagem = personagemService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPersonagem);
    }

    @PostMapping("/completo")
    public ResponseEntity<PersonagemResponseDTO> criarCompleto(@RequestBody @Valid PersonagemCompletoCreateDTO dto) {
        PersonagemResponseDTO novoPersonagem = personagemService.criarCompleto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPersonagem);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonagemResponseDTO> buscarPorId(@PathVariable Long id) {
        PersonagemResponseDTO dto = personagemService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/campanha/{campanhaId}")
    public ResponseEntity<List<PersonagemResponseDTO>> listarPorCampanha(@PathVariable Long campanhaId) {
        List<PersonagemResponseDTO> lista = personagemService.listarPorCampanha(campanhaId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PersonagemResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<PersonagemResponseDTO> lista = personagemService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/campanha/{campanhaId}/ativo")
    public ResponseEntity<PersonagemResponseDTO> buscarAtivoDoJogador(@PathVariable Long campanhaId,
                                                                      @AuthenticationPrincipal UsuarioPrincipal principal) {
        PersonagemResponseDTO dto = personagemService.buscarAtivoDoJogador(campanhaId, principal.getId());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PersonagemResponseDTO> atualizar(@PathVariable Long id,
                                                           @RequestBody PersonagemUpdateDTO dto) {
        PersonagemResponseDTO atualizado = personagemService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        personagemService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/meu-personagem")
    public ResponseEntity<PersonagemResponseDTO> meuPersonagem(@PathVariable Long campanhaId,
                                                               @AuthenticationPrincipal UsuarioPrincipal principal) {
        PersonagemResponseDTO dto = personagemService.buscarAtivoDoJogador(campanhaId, principal.getId());
        return ResponseEntity.ok(dto);
    }
}