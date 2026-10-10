package com.draftstd.hydraforge.adapters.in.rest.controller.sistema;

import com.draftstd.hydraforge.adapters.in.dto.sistema.SistemaCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.sistema.SistemaUpdateDTO;
import com.draftstd.hydraforge.ports.in.command.sistema.AtualizarSistemaCommand;
import com.draftstd.hydraforge.ports.in.command.sistema.CriarSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.ListarSistemasQuery;
import com.draftstd.hydraforge.ports.in.query.sistema.ObterSistemaQuery;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;
import com.draftstd.hydraforge.ports.in.sistema.AtualizarSistemaUseCase;
import com.draftstd.hydraforge.ports.in.sistema.CriarSistemaUseCase;
import com.draftstd.hydraforge.ports.in.sistema.ObterSistemaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sistemas")
public class SistemaController {

    private final CriarSistemaUseCase criarSistemaUseCase;
    private final AtualizarSistemaUseCase atualizarSistemaUseCase;
    private final ObterSistemaUseCase obterSistemaUseCase;

    public SistemaController(CriarSistemaUseCase criarSistemaUseCase,
                             AtualizarSistemaUseCase atualizarSistemaUseCase,
                             ObterSistemaUseCase obterSistemaUseCase) {
        this.criarSistemaUseCase = criarSistemaUseCase;
        this.atualizarSistemaUseCase = atualizarSistemaUseCase;
        this.obterSistemaUseCase = obterSistemaUseCase;
    }

    @PostMapping
    public ResponseEntity<SistemaQueryResponse> criar(@Valid @RequestBody SistemaCreateDTO dto,
                                                      @RequestHeader("X-Usuario-Id") UUID autorId) {
        CriarSistemaCommand command = new CriarSistemaCommand(
                dto.nome(),
                dto.descricao(),
                dto.versao(),
                autorId,
                dto.regrasBaseJson()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(criarSistemaUseCase.executar(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SistemaQueryResponse> atualizar(@PathVariable UUID id,
                                                           @Valid @RequestBody SistemaUpdateDTO dto) {
        AtualizarSistemaCommand command = new AtualizarSistemaCommand(
                id,
                dto.nome(),
                dto.descricao(),
                dto.versao(),
                dto.regrasBaseJson()
        );
        return ResponseEntity.ok(atualizarSistemaUseCase.executar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SistemaQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterSistemaQuery query = new ObterSistemaQuery(id);
        return ResponseEntity.ok(obterSistemaUseCase.obterPorId(query));
    }

    @GetMapping
    public ResponseEntity<List<SistemaQueryResponse>> listarSistemas(@RequestParam(defaultValue = "true") Boolean apenasAtivos) {
        ListarSistemasQuery query = new ListarSistemasQuery(apenasAtivos);
        return ResponseEntity.ok(obterSistemaUseCase.listarSistemas(query));
    }
}