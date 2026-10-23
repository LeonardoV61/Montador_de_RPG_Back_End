package com.draftstd.hydraforge.adapters.out.persistence.sistema.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.SistemaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper.SistemaMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.SistemaRepository;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ObterSistemaPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.SalvarSistemaPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SistemaPersistenceAdapter implements SalvarSistemaPort, ObterSistemaPort {

    private final SistemaRepository sistemaRepository;
    private final SistemaMapper sistemaMapper;

    public SistemaPersistenceAdapter(SistemaRepository sistemaRepository, SistemaMapper sistemaMapper) {
        this.sistemaRepository = sistemaRepository;
        this.sistemaMapper = sistemaMapper;
    }

    @Override
    public void salvar(Sistema sistema) {
        SistemaJpaEntity entity = sistemaMapper.toEntity(sistema);
        sistemaRepository.save(entity);
    }

    @Override
    public void deletarPorId(UUID id) {
        if (!sistemaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Sistema não encontrado para exclusão: " + id);
        }
        sistemaRepository.deleteById(id);
    }

    @Override
    public Optional<Sistema> buscarPorId(UUID id) {
        return sistemaRepository.findById(id).map(sistemaMapper::toDomain);
    }

    @Override
    public List<Sistema> listarTodos() {
        return sistemaRepository.findAll().stream().map(sistemaMapper::toDomain).toList();
    }
}