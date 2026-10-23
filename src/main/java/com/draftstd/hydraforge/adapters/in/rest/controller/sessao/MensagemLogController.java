package com.draftstd.hydraforge.adapters.in.rest.controller.sessao;

import com.draftstd.hydraforge.adapters.in.dto.sessao.MensagemCreateDTO;
import com.draftstd.hydraforge.adapters.in.dto.sessao.MensagemResponseDTO;
import com.draftstd.hydraforge.application.request.sessao.CriarMensagemLogCommand;
import com.draftstd.hydraforge.application.response.sessao.ObterMensagensLogQuery;
import com.draftstd.hydraforge.ports.in.sessao.CriarMensagemLogUseCase;
import com.draftstd.hydraforge.ports.in.sessao.ObterMensagensLogUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessoes/{sessaoId}/mensagens")
public class MensagemLogController {

    private final ObterMensagensLogUseCase obterMensagensLogUseCase;
    private final CriarMensagemLogUseCase criarMensagemLogUseCase;

    public MensagemLogController(
            ObterMensagensLogUseCase obterMensagensLogUseCase,
            CriarMensagemLogUseCase criarMensagemLogUseCase
    ) {
        this.obterMensagensLogUseCase = obterMensagensLogUseCase;
        this.criarMensagemLogUseCase = criarMensagemLogUseCase;
    }

    @PostMapping
    public ResponseEntity<MensagemResponseDTO> criar(
            @PathVariable UUID sessaoId,
            @Valid @RequestBody MensagemCreateDTO dto,
            @RequestHeader(value = "X-Usuario-Id", required = false) UUID emissorIdHeader
    ) {
        UUID emissorId = dto.usuarioId() != null ? dto.usuarioId() : emissorIdHeader;
        CriarMensagemLogCommand command = new CriarMensagemLogCommand(sessaoId, emissorId, dto.conteudo());
        
        // O UseCase retorna o UUID da mensagem recém-criada
        UUID mensagemId = criarMensagemLogUseCase.executar(command);

        // Monta o DTO de resposta do controller com a hora atual
        MensagemResponseDTO dtoResponse = new MensagemResponseDTO(
                mensagemId,
                sessaoId,
                emissorId,
                dto.conteudo(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }

    @GetMapping
    public ResponseEntity<List<MensagemResponseDTO>> listarMensagens(
            @PathVariable UUID sessaoId,
            @RequestParam(defaultValue = "50") Integer limite
    ) {
        ObterMensagensLogQuery query = new ObterMensagensLogQuery(sessaoId, limite);
        var respostas = obterMensagensLogUseCase.listarPorSessao(query);

        // Mapeamento tratando possíveis variações de nomes nos getters do record/objeto de query response
        List<MensagemResponseDTO> mensagens = respostas.stream().map(m -> new MensagemResponseDTO(
                m.id(),
                m.sessaoId(),
                m.emissorId(),
                m.conteudo(),
                m.enviadaEm() // Ajustado para enviadaEm() conforme o modelo de resposta da query
        )).toList();

        return ResponseEntity.ok(mensagens);
    }
}