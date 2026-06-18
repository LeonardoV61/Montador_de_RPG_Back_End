// ResolucaoService.java
package com.rpgvtt.montador_de_rpg_backend.service.mecanica;

import com.rpgvtt.montador_de_rpg_backend.domain.model.mecanica.Resolucao;
import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.Sistema;
import com.rpgvtt.montador_de_rpg_backend.dto.mecanica.ResolucaoCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.mecanica.ResolucaoResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.mecanica.ResolucaoUpdateDTO;
import com.rpgvtt.montador_de_rpg_backend.repository.mecanica.ResolucaoRepository;
import com.rpgvtt.montador_de_rpg_backend.repository.sistema.SistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import tools.jackson.databind.JsonNode;
import com.rpgvtt.montador_de_rpg_backend.service.mecanica.ResolutionEvaluator;
import com.rpgvtt.montador_de_rpg_backend.dto.mecanica.ResolucaoExecucaoDTO;

@Service
@RequiredArgsConstructor
public class ResolucaoService {

    private final ResolucaoRepository resolucaoRepository;
    private final SistemaRepository sistemaRepository;
    private final ResolutionEvaluator resolutionEvaluator;

    @Transactional
    public ResolucaoResponseDTO criar(ResolucaoCreateDTO dto) {
        Sistema sistema = sistemaRepository.findById(dto.sistemaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sistema não encontrado."));

        Resolucao resolucao = new Resolucao();
        resolucao.setSistema(sistema);
        resolucao.setNome(dto.nome());
        resolucao.setTipo(dto.tipo());
        resolucao.setParametros(dto.parametros());

        return mapearParaDTO(resolucaoRepository.save(resolucao));
    }

    @Transactional(readOnly = true)
    public ResolucaoResponseDTO buscarPorId(Long id) {
        Resolucao resolucao = resolucaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resolução não encontrada."));
        return mapearParaDTO(resolucao);
    }

    @Transactional(readOnly = true)
    public List<ResolucaoResponseDTO> listarPorSistema(Long sistemaId) {
        if (!sistemaRepository.existsById(sistemaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sistema não encontrado.");
        }
        return resolucaoRepository.findBySistemaId(sistemaId)
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    @Transactional
    public ResolucaoResponseDTO atualizar(Long id, ResolucaoUpdateDTO dto) {
        Resolucao resolucao = resolucaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resolução não encontrada."));

        if (dto.nome() != null)       resolucao.setNome(dto.nome());
        if (dto.parametros() != null) resolucao.setParametros(dto.parametros());

        return mapearParaDTO(resolucaoRepository.save(resolucao));
    }

    @Transactional
    public void deletar(Long id) {
        if (!resolucaoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resolução não encontrada.");
        }
        resolucaoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ResolucaoExecucaoDTO executar(Long id, JsonNode contexto) {
        Resolucao resolucao = resolucaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resolução não encontrada."));

        var outcome = resolutionEvaluator.evaluate(resolucao, contexto);

        return new ResolucaoExecucaoDTO(
                outcome.roll(),
                outcome.targetValue(),
                outcome.success(),
                outcome.motivo(),
                outcome.detalhes()
        );
    }

    private ResolucaoResponseDTO mapearParaDTO(Resolucao resolucao) {
        return new ResolucaoResponseDTO(
                resolucao.getId(),
                resolucao.getSistema().getId(),
                resolucao.getSistema().getNome(),
                resolucao.getNome(),
                resolucao.getTipo(),
                resolucao.getParametros()
        );
    }
}