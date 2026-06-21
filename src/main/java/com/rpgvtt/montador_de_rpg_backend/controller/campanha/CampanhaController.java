package com.rpgvtt.montador_de_rpg_backend.controller.campanha;

import com.rpgvtt.montador_de_rpg_backend.dto.campanha.CampanhaCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.campanha.CampanhaResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.repository.usuario.UsuarioRepository;
import com.rpgvtt.montador_de_rpg_backend.domain.model.usuario.Usuario;
import com.rpgvtt.montador_de_rpg_backend.dto.campanha.AdicionarJogadorDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.campanha.CampanhaParticipanteResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.service.campanha.CampanhaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campanhas")
public class CampanhaController {

    private final CampanhaService campanhaService;
    private final UsuarioRepository usuarioRepository;

    public CampanhaController(CampanhaService campanhaService, UsuarioRepository usuarioRepository) {
        this.campanhaService = campanhaService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/{campanhaId}/jogadores")
    public ResponseEntity<CampanhaParticipanteResponseDTO> adicionarJogador(@PathVariable Long campanhaId, @RequestBody @Valid AdicionarJogadorDTO dto) {
        CampanhaParticipanteResponseDTO participante = campanhaService.adicionarJogador(campanhaId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(participante);
    }

    @PostMapping
    public ResponseEntity<CampanhaResponseDTO> criar(
            @RequestBody @Valid CampanhaCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) { // 👈 pega do JWT

        // Busca o usuário autenticado pelo email/username do token
        Usuario criador = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        CampanhaResponseDTO novaCampanha = campanhaService.criar(dto, criador.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCampanha);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampanhaResponseDTO> buscarPorId(@PathVariable Long id) {
        CampanhaResponseDTO dto = campanhaService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<CampanhaResponseDTO>> listarTodas() {
        List<CampanhaResponseDTO> lista = campanhaService.listarTodas();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        campanhaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}