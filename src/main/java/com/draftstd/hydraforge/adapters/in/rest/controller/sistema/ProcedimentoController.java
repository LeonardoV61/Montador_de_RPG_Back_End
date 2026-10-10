package com.draftstd.hydraforge.adapters.in.rest.controller.sistema;

import com.draftstd.hydraforge.adapters.in.dto.sistema.ProcedimentoContextoDTO;
import com.draftstd.hydraforge.adapters.in.dto.sistema.RespostaEtapaDTO;
import com.draftstd.hydraforge.ports.in.command.sistema.ExecutarProcedimentoCommand;
import com.draftstd.hydraforge.ports.in.sistema.GerenciarProcedimentoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/procedimentos")
public class ProcedimentoController {

    private final GerenciarProcedimentoUseCase gerenciarProcedimentoUseCase;

    public ProcedimentoController(GerenciarProcedimentoUseCase gerenciarProcedimentoUseCase) {
        this.gerenciarProcedimentoUseCase = gerenciarProcedimentoUseCase;
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Object> iniciarProcedimento(@PathVariable UUID id,
                                                      @RequestParam UUID sessaoId,
                                                      @RequestBody(required = false) List<UUID> instanciasIds) {
        ExecutarProcedimentoCommand command = new ExecutarProcedimentoCommand(
                id,
                sessaoId,
                instanciasIds != null ? instanciasIds : List.of(),
                "INICIAR",
                Map.of()
        );
        Object resultado = gerenciarProcedimentoUseCase.executarProcedimento(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PostMapping("/{id}/responder")
    public ResponseEntity<Object> responderEtapa(@PathVariable UUID id,
                                                 @RequestParam UUID sessaoId,
                                                 @Valid @RequestBody RespostaEtapaDTO dto) {
        ExecutarProcedimentoCommand command = new ExecutarProcedimentoCommand(
                id,
                sessaoId,
                List.of(),
                "RESPONDER",
                Map.of("resposta", dto.getValor())
        );
        Object resultado = gerenciarProcedimentoUseCase.executarProcedimento(command);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{sessaoId}/status")
    public ResponseEntity<Object> obterStatusProcedimentoAtivo(@PathVariable UUID sessaoId) {
        ExecutarProcedimentoCommand command = new ExecutarProcedimentoCommand(
                null,
                sessaoId,
                List.of(),
                "OBTER_STATUS",
                Map.of()
        );
        Object resultado = gerenciarProcedimentoUseCase.executarProcedimento(command);
        return ResponseEntity.ok(resultado);
    }
}