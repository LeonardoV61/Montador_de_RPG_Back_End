package com.draftstd.hydraforge.adapters.in.rest.controller.sessao;

import com.draftstd.hydraforge.adapters.in.dto.sessao.SessaoCreateDTO;
import com.draftstd.hydraforge.application.request.sessao.AlterarStatusSessaoCommand;
import com.draftstd.hydraforge.application.request.sessao.CriarSessaoCommand;
import com.draftstd.hydraforge.application.response.sessao.ObterDetalhesSessaoQuery;
import com.draftstd.hydraforge.application.response.sessao.SessaoQueryResponse;
import com.draftstd.hydraforge.domain.enums.StatusSessao;
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

    public SessaoController(
            CriarSessaoUseCase criarSessaoUseCase,
            ObterDetalhesSessaoUseCase obterDetalhesSessaoUseCase,
            GerenciarStatusSessaoUseCase gerenciarStatusSessaoUseCase
    ) {
        this.criarSessaoUseCase = criarSessaoUseCase;
        this.obterDetalhesSessaoUseCase = obterDetalhesSessaoUseCase;
        this.gerenciarStatusSessaoUseCase = gerenciarStatusSessaoUseCase;
    }

    @PostMapping
    public ResponseEntity<SessaoQueryResponse> criar(@Valid @RequestBody SessaoCreateDTO dto) {
        // Definindo valores padrão para 'titulo' e 'ordem' caso o DTO traga apenas 'campanhaId' e 'dataInicio'
        String tituloPadrao = "Nova Sessão";
        int ordemPadrao = 1;

        CriarSessaoCommand command = new CriarSessaoCommand(
                dto.campanhaId(),
                tituloPadrao,
                ordemPadrao,
                dto.dataInicio()
        );

        // O UseCase executa o comando e retorna o UUID da sessão criada
        UUID sessaoId = criarSessaoUseCase.executar(command);

        // Consulta os detalhes da sessão para montar a resposta
        SessaoQueryResponse response = obterDetalhesSessaoUseCase.obterPorId(new ObterDetalhesSessaoQuery(sessaoId));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessaoQueryResponse> obterPorId(@PathVariable UUID id) {
        SessaoQueryResponse response = obterDetalhesSessaoUseCase.obterPorId(new ObterDetalhesSessaoQuery(id));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SessaoQueryResponse> alterarStatus(
            @PathVariable UUID id,
            @RequestParam StatusSessao novoStatus,
            @RequestBody(required = false) String snapshotFinalJson
    ) {
        // Trata o JSON do snapshot como opcional ou string vazia por padrao
        String snapshot = snapshotFinalJson != null ? snapshotFinalJson : "";

        AlterarStatusSessaoCommand command = new AlterarStatusSessaoCommand(id, novoStatus, snapshot);
        
        // A porta executa o comando de alteracao (retorno void)
        gerenciarStatusSessaoUseCase.alterarStatus(command);

        // Busca os detalhes atualizados apos a mudanca de status
        SessaoQueryResponse response = obterDetalhesSessaoUseCase.obterPorId(new ObterDetalhesSessaoQuery(id));
        return ResponseEntity.ok(response);
    }
}