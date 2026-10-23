package com.draftstd.hydraforge.adapters.in.rest.controller.sessao;

import com.draftstd.hydraforge.adapters.in.dto.sessao.CenaCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.sessao.CenaUpdateDTO;
import com.draftstd.hydraforge.application.request.sessao.AtualizarCenaCommand;
import com.draftstd.hydraforge.application.request.sessao.CriarCenaCommand;
import com.draftstd.hydraforge.application.response.sessao.CenaQueryResponse;
import com.draftstd.hydraforge.application.response.sessao.ObterCenaQuery;
import com.draftstd.hydraforge.ports.in.sessao.CriarCenaUseCase;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarCenaUseCase;
import com.draftstd.hydraforge.ports.in.sessao.ObterCenaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cenas")
public class CenaController {

    private final CriarCenaUseCase criarCenaUseCase;
    private final GerenciarCenaUseCase gerenciarCenaUseCase;
    private final ObterCenaUseCase obterCenaUseCase;

    public CenaController(
            CriarCenaUseCase criarCenaUseCase,
            GerenciarCenaUseCase gerenciarCenaUseCase,
            ObterCenaUseCase obterCenaUseCase
    ) {
        this.criarCenaUseCase = criarCenaUseCase;
        this.gerenciarCenaUseCase = gerenciarCenaUseCase;
        this.obterCenaUseCase = obterCenaUseCase;
    }

    @PostMapping
    public ResponseEntity<CenaQueryResponse> criar(@Valid @RequestBody CenaCreateDTO dto) {
        String estadoJson = dto.estado() != null ? dto.estado().toString() : "{}";
        Integer ordem = dto.ordem() != null ? dto.ordem() : 0;
        String tipo = dto.tipo() != null ? dto.tipo() : "PADRAO";

        CriarCenaCommand command = new CriarCenaCommand(
                dto.sessaoId(),
                "{}",             // mapaJson padrao
                dto.mapaUrl(),
                ordem,
                tipo,
                estadoJson
        );

        // O UseCase retorna o UUID da nova cena
        UUID cenaId = criarCenaUseCase.executar(command);

        // Consulta a cena criada para retornar o CenaQueryResponse
        ObterCenaQuery query = new ObterCenaQuery(cenaId);
        CenaQueryResponse response = obterCenaUseCase.obterPorId(query);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CenaQueryResponse> atualizar(@PathVariable UUID id, @Valid @RequestBody CenaUpdateDTO dto) {
        String estadoJson = dto.estado() != null ? dto.estado().toString() : "{}";
        Integer ordem = dto.ordem() != null ? dto.ordem() : 0;
        String tipo = dto.tipo() != null ? dto.tipo() : "PADRAO";

        AtualizarCenaCommand command = new AtualizarCenaCommand(
                id,
                "{}",             // mapaJson padrao
                dto.mapaUrl(),
                ordem,
                tipo,
                estadoJson
        );

        gerenciarCenaUseCase.atualizarCena(command);

        ObterCenaQuery query = new ObterCenaQuery(id);
        CenaQueryResponse response = obterCenaUseCase.obterPorId(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CenaQueryResponse> obterPorId(@PathVariable UUID id) {
        ObterCenaQuery query = new ObterCenaQuery(id);
        return ResponseEntity.ok(obterCenaUseCase.obterPorId(query));
    }
}