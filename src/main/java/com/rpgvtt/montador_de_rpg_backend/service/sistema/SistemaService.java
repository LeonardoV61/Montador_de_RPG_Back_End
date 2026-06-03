package com.rpgvtt.montador_de_rpg_backend.service.sistema;

import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.Sistema;
import com.rpgvtt.montador_de_rpg_backend.domain.model.usuario.Usuario;
import com.rpgvtt.montador_de_rpg_backend.domain.validation.SchemaValidator;
import com.rpgvtt.montador_de_rpg_backend.dto.sistema.SistemaCreateDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.sistema.SistemaResponseDTO;
import com.rpgvtt.montador_de_rpg_backend.dto.sistema.SistemaUpdateDTO;
import com.rpgvtt.montador_de_rpg_backend.repository.sistema.SistemaRepository;
import com.rpgvtt.montador_de_rpg_backend.repository.usuario.UsuarioRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SistemaService {

    private final SistemaRepository sistemaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SchemaValidator schemaValidator;

    @SuppressWarnings("unused")
    private final EntityManager entityManager;

    @Transactional
    public SistemaResponseDTO criar(SistemaCreateDTO dto) {

        Usuario criador = usuarioRepository.findById(dto.criadorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        Sistema sistema = new Sistema();
        sistema.setUsuario(criador);
        sistema.setNome(dto.nome());
        sistema.setDescricao(dto.descricao());
        sistema.setUrlImagem(dto.urlImagem());
        sistema.setConfiguracao(dto.configuracao());
        sistema.setSchemaAtributos(dto.schemaAtributos());
        sistema.setSchemaEntidades(dto.schemaEntidades());
        sistema.setVersaoSchemas(1);
        sistema.setEOficial(false);

        
        if (dto.sistemaPaiId() != null) {
            Sistema pai = sistemaRepository.findById(dto.sistemaPaiId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sistema pai não encontrado."));
            sistema.setSistemaPai(pai);
        }

        schemaValidator.validarConsistenciaSchemas(sistema);

        return mapearParaDTO(sistemaRepository.save(sistema));
    }

    @Transactional(readOnly = true)
    public SistemaResponseDTO buscarPorId(Long id) {
        Sistema sistema = sistemaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sistema não encontrado."));
        return mapearParaDTO(sistema);
    }

    @Transactional(readOnly = true)
    public List<SistemaResponseDTO> listarTodos() {
        return sistemaRepository.findAll().stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    @Transactional
    public SistemaResponseDTO atualizar(Long id, SistemaUpdateDTO dto) {
        Sistema sistema = sistemaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sistema não encontrado."));

        if (dto.nome() != null)        sistema.setNome(dto.nome());
        if (dto.descricao() != null)   sistema.setDescricao(dto.descricao());
        if (dto.urlImagem() != null)   sistema.setUrlImagem(dto.urlImagem());
        if (dto.configuracao() != null) sistema.setConfiguracao(dto.configuracao());

        
        boolean schemasMudaram = false;

        if (dto.schemaAtributos() != null) {
            sistema.setSchemaAtributos(dto.schemaAtributos());
            schemasMudaram = true;
        }
        if (dto.schemaEntidades() != null) {
            sistema.setSchemaEntidades(dto.schemaEntidades());
            schemasMudaram = true;
        }

        if (schemasMudaram) {
            schemaValidator.validarConsistenciaSchemas(sistema);
            sistema.setVersaoSchemas(sistema.getVersaoSchemas() + 1);
        }

        return mapearParaDTO(sistemaRepository.save(sistema));
    }

    @Transactional
    public SistemaResponseDTO marcarComoOficial(Long id) {
        
        Sistema sistema = sistemaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sistema não encontrado."));

        if (sistema.isEOficial()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este sistema já é oficial.");
        }

        sistema.setEOficial(true);
        return mapearParaDTO(sistemaRepository.save(sistema));
    }

    @Transactional
    public void deletar(Long id) {
        if (!sistemaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sistema não encontrado.");
        }
        sistemaRepository.deleteById(id);
    }

    private SistemaResponseDTO mapearParaDTO(Sistema sistema) {
        return new SistemaResponseDTO(
                sistema.getId(),
                sistema.getUsuario().getId(),
                sistema.getUsuario().getApelido(),
                
                sistema.getSistemaPai() != null ? sistema.getSistemaPai().getId() : null,
                sistema.getNome(),
                sistema.getDescricao(),
                sistema.getUrlImagem(),
                sistema.getConfiguracao(),
                sistema.getSchemaAtributos(),
                sistema.getSchemaEntidades(),
                sistema.getVersaoSchemas(),
                sistema.isEOficial(),
                sistema.getCriadoEm()
        );
    }
}