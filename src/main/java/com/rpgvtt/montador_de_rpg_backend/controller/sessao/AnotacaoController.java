package com.rpgvtt.montador_de_rpg_backend.controller.sessao;

import com.rpgvtt.montador_de_rpg_backend.dto.sessao.AnotacaoCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.sessao.AnotacaoResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.sessao.AnotacaoUpdateDTO;
import com.rpgvtt.montador_de_rpg_backend.service.sessao.AnotacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anotacoes")
@RequiredArgsConstructor
public class AnotacaoController {

    private final AnotacaoService service;

    @PostMapping
    public ResponseEntity<AnotacaoResponseDTO> criar(@RequestBody AnotacaoCreateDTO dto) {
        AnotacaoResponseDTO created = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnotacaoResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/sessao/{idSessao}")
    public ResponseEntity<List<AnotacaoResponseDTO>> listBySessao(@PathVariable Long idSessao) {
        return ResponseEntity.ok(service.listarPorSessao(idSessao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnotacaoResponseDTO> atualizar(@PathVariable Integer id,
                                                         @RequestBody AnotacaoUpdateDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
