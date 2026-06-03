package com.rpgvtt.montador_de_rpg_backend.service.sistema;

import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.EventoSistema;
import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.Sistema;
import com.rpgvtt.montador_de_rpg_backend.dto.sistema.EventoSistemaCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.sistema.EventoSistemaResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.sistema.EventoSistemaUpdateDTO;
import com.rpgvtt.montador_de_rpg_backend.repository.sistema.EventoSistemaRepository;
import com.rpgvtt.montador_de_rpg_backend.repository.sistema.SistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoSistemaService {

    private final EventoSistemaRepository eventoRepository;
    private final SistemaRepository sistemaRepository;

    @Transactional
    public EventoSistemaResponseDTO criar(EventoSistemaCreateDTO dto) {
        Sistema sistema = sistemaRepository.findById(dto.sistemaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sistema não encontrado."));

        EventoSistema evento = new EventoSistema();
        evento.setSistema(sistema);
        evento.setNome(dto.nome());
        evento.setDescricao(dto.descricao());
        evento.setPayloadSchema(dto.payloadSchema());

        return mapearParaDTO(eventoRepository.save(evento));
    }

    @Transactional(readOnly = true)
    public EventoSistemaResponseDTO buscarPorId(Long id) {
        EventoSistema evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));
        return mapearParaDTO(evento);
    }

    @Transactional(readOnly = true)
    public List<EventoSistemaResponseDTO> listarPorSistema(Long sistemaId) {
        
        if (!sistemaRepository.existsById(sistemaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sistema não encontrado.");
        }
        return eventoRepository.findBySistemaId(sistemaId)
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    @Transactional
    public EventoSistemaResponseDTO atualizar(Long id, EventoSistemaUpdateDTO dto) {
        EventoSistema evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));

        if (dto.nome() != null)          evento.setNome(dto.nome());
        if (dto.descricao() != null)     evento.setDescricao(dto.descricao());
        if (dto.payloadSchema() != null) evento.setPayloadSchema(dto.payloadSchema());

        return mapearParaDTO(eventoRepository.save(evento));
    }

    @Transactional
    public void deletar(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado.");
        }
        eventoRepository.deleteById(id);
    }

    private EventoSistemaResponseDTO mapearParaDTO(EventoSistema evento) {
        return new EventoSistemaResponseDTO(
                evento.getId(),
                evento.getSistema().getId(),
                evento.getSistema().getNome(),
                evento.getNome(),
                evento.getDescricao(),
                evento.getPayloadSchema()
        );
    }
}