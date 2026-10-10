package com.draftstd.hydraforge.adapters.in.rest.controller.sessao;

import com.draftstd.hydraforge.adapters.in.dto.sessao.MensagemResponseDTO;
import com.draftstd.hydraforge.ports.in.query.sessao.ObterDetalhesSessaoQuery;
import com.draftstd.hydraforge.ports.in.sessao.ObterDetalhesSessaoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessoes/{sessaoId}/mensagens")
public class MensagemLogController {

    private final ObterDetalhesSessaoUseCase obterDetalhesSessaoUseCase;

    public MensagemLogController(ObterDetalhesSessaoUseCase obterDetalhesSessaoUseCase) {
        this.obterDetalhesSessaoUseCase = obterDetalhesSessaoUseCase;
    }

    @GetMapping
    public ResponseEntity<List<MensagemResponseDTO>> listarMensagens(@PathVariable UUID sessaoId,
                                                                     @RequestParam(defaultValue = "50") Integer limite) {
        ObterDetalhesSessaoQuery query = new ObterDetalhesSessaoQuery(sessaoId);
        var detalhes = obterDetalhesSessaoUseCase.obterPorId(query);
        
        // Mapeamento das mensagens armazenadas na sessão para o DTO de resposta
        List<MensagemResponseDTO> mensagens = detalhes.mensagens().stream()
                .limit(limite)
                .map(m -> new MensagemResponseDTO(
                        m.id(),
                        m.sessaoId(),
                        m.emissorId(),
                        m.conteudo(),
                        m.enviadaEm()
                ))
                .toList();

        return ResponseEntity.ok(mensagens);
    }
}