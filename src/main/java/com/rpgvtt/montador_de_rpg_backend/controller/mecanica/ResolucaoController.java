package com.rpgvtt.montador_de_rpg_backend.controller.mecanica;

import com.rpgvtt.montador_de_rpg_backend.dto.mecanica.ResolucaoExecucaoDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.mecanica.ResolucaoResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.mecanica.ResolucaoCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.mecanica.ResolucaoUpdateDTO;
import com.rpgvtt.montador_de_rpg_backend.service.mecanica.ResolucaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

import java.util.List;

@RestController
@RequestMapping("/api/resolucoes")
public class ResolucaoController {

    private final ResolucaoService resolucaoService;

    public ResolucaoController(ResolucaoService resolucaoService) {
        this.resolucaoService = resolucaoService;
    }

    @PostMapping
    public ResponseEntity<ResolucaoResponseDTO> criar(@RequestBody @Valid ResolucaoCreateDTO dto) {
        ResolucaoResponseDTO novaResolucao = resolucaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaResolucao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResolucaoResponseDTO> buscarPorId(@PathVariable Long id) {
        ResolucaoResponseDTO dto = resolucaoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/sistema/{sistemaId}")
    public ResponseEntity<List<ResolucaoResponseDTO>> listarPorSistema(@PathVariable Long sistemaId) {
        List<ResolucaoResponseDTO> lista = resolucaoService.listarPorSistema(sistemaId);
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResolucaoResponseDTO> atualizar(@PathVariable Long id,
                                                          @RequestBody ResolucaoUpdateDTO dto) {
        ResolucaoResponseDTO atualizado = resolucaoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        resolucaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/executar")
    public ResponseEntity<ResolucaoExecucaoDTO> executar(@PathVariable Long id,
                                                         @RequestBody JsonNode contexto) {
        ResolucaoExecucaoDTO resultado = resolucaoService.executar(id, contexto);
        return ResponseEntity.ok(resultado);
    }
}