package com.rpgvtt.montador_de_rpg_backend.controller.entidade;

import com.rpgvtt.montador_de_rpg_backend.dto.entidade.EntidadeInstanciaCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.entidade.EntidadeInstanciaResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.entidade.EntidadeInstanciaUpadteDTO;
import com.rpgvtt.montador_de_rpg_backend.service.entidade.EntidadeInstanciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entidades-instancia")
public class EntidadeInstanciaController {

    private final EntidadeInstanciaService entidadeInstanciaService;

    public EntidadeInstanciaController(EntidadeInstanciaService entidadeInstanciaService) {
        this.entidadeInstanciaService = entidadeInstanciaService;
    }

    @PostMapping
    public ResponseEntity<EntidadeInstanciaResponseDTO> criar(@RequestBody @Valid EntidadeInstanciaCreateDTO dto) {
        EntidadeInstanciaResponseDTO novaInstancia = entidadeInstanciaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaInstancia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntidadeInstanciaResponseDTO> buscarPorId(@PathVariable Long id) {
        EntidadeInstanciaResponseDTO dto = entidadeInstanciaService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/campanha/{campanhaId}")
    public ResponseEntity<List<EntidadeInstanciaResponseDTO>> listarPorCampanha(@PathVariable Long campanhaId) {
        List<EntidadeInstanciaResponseDTO> lista = entidadeInstanciaService.listarPorCampanha(campanhaId);
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntidadeInstanciaResponseDTO> atualizar(@PathVariable Long id,
                                                                  @RequestBody EntidadeInstanciaUpadteDTO dto) {
        EntidadeInstanciaResponseDTO atualizado = entidadeInstanciaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        entidadeInstanciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}