package com.draftstd.hydraforge.adapters.in.rest.controller.sessao;

import com.draftstd.hydraforge.adapters.in.dto.sessao.EntradaSessaoDTO;
import com.draftstd.hydraforge.adapters.in.dto.sessao.SessaoCreateDTO;
import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.ports.in.command.sessao.AlterarStatusSessaoCommand;
import com.draftstd.hydraforge.ports.in.command.sessao.CriarSessaoCommand;
import com.draftstd.hydraforge.ports.in.command.sessao.EntrarSessaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterDetalhesSessaoQuery;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.CriarSessaoUseCase;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarStatusSessaoUseCase;
import com.draftstd.hydraforge.ports.in.sessao.ObterDetalhesSessaoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessoes")
public class SessaoController {

    private final CriarSessaoUseCase criarSessaoUseCase;
    private final ObterDetalhesSessaoUseCase obterDetalhesSessaoUseCase;
    private final GerenciarStatusSessaoUseCase gerenciarStatusSessaoUseCase;

    public SessaoController(CriarSessaoUseCase criarSessaoUseCase,
                            ObterDetalhesSessaoUseCase obterDetalhesSessaoUseCase,
                            GerenciarStatusSessaoUseCase gerenciarStatusSessaoUseCase) {
        this.criarSessaoUseCase = criarSessaoUseCase;
        this.obterDetalhesSessaoUseCase = obterDetalhesSessaoUseCase;
        this.gerenciarStatusSessaoUseCase = gerenciarStatusSessaoUseCase;
    }

    @PostMapping
    public ResponseEntity<SessaoQueryResponse> criar(@Valid @RequestBody SessaoCreateDTO dto) {
        CriarSessaoCommand command = new CriarSessaoCommand(
                dto.campanhaId(),
                dto.titulo(),
                dto.descricao(),
                dto.agendadaPara()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(criarSessaoUseCase.executar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterDetalhesSessaoQuery query = new ObterDetalhesSessaoQuery(id);
        return ResponseEntity.ok(obterDetalhesSessaoUseCase.obterPorId(query));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SessaoQueryResponse> alterarStatus(@PathVariable UUID id,
                                                             @RequestParam StatusSessao status) {
        AlterarStatusSessaoCommand command = new AlterarStatusSessaoCommand(id, status);
        return ResponseEntity.ok(gerenciarStatusSessaoUseCase.alterarStatus(command));
    }
}