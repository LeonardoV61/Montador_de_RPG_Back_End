package com.draftstd.hydraforge.adapters.in.rest.controller.mecanica;

import com.draftstd.hydraforge.adapters.in.dto.mecanica.TesteLivreRequestDTO;
import com.draftstd.hydraforge.ports.in.command.mecanica.RolarDadosCommand;
import com.draftstd.hydraforge.ports.in.mecanica.ObterHistoricoRolagensUseCase;
import com.draftstd.hydraforge.ports.in.mecanica.RolarDadosUseCase;
import com.draftstd.hydraforge.ports.in.query.mecanica.ObterHistoricoRolagensQuery;
import com.draftstd.hydraforge.ports.in.query.mecanica.ResultadoRolagemQueryResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mecanica")
public class ResolucaoController {

    private final RolarDadosUseCase rolarDadosUseCase;
    private final ObterHistoricoRolagensUseCase obterHistoricoRolagensUseCase;

    public ResolucaoController(RolarDadosUseCase rolarDadosUseCase, ObterHistoricoRolagensUseCase obterHistoricoRolagensUseCase) {
        this.rolarDadosUseCase = rolarDadosUseCase;
        this.obterHistoricoRolagensUseCase = obterHistoricoRolagensUseCase;
    }

    @PostMapping("/rolar")
    public ResponseEntity<ResultadoRolagemQueryResponse> rolarDados(@Valid @RequestBody TesteLivreRequestDTO dto, @RequestHeader("X-Usuario-Id") UUID usuarioId) {
        RolarDadosCommand command = new RolarDadosCommand(dto.sessaoId(), usuarioId, dto.personagemId(), dto.expressaoDados(), dto.motivo(), dto.contextoAdicional());
        return ResponseEntity.ok(rolarDadosUseCase.executar(command));
    }

    @GetMapping("/historico/sessao/{sessaoId}")
    public ResponseEntity<List<ResultadoRolagemQueryResponse>> obterHistoricoSessao(@PathVariable UUID sessaoId, @RequestParam(required = false) UUID personagemId, @RequestParam(defaultValue = "50") Integer limite) {
        ObterHistoricoRolagensQuery query = new ObterHistoricoRolagensQuery(sessaoId, personagemId, limite);
        return ResponseEntity.ok(obterHistoricoRolagensUseCase.executar(query));
    }
}