package com.draftstd.hydraforge.adapters.out.persistence.mecanica.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.mecanica.repository.PrimitivoRepository;
import com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity.PrimitivoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.mecanica.mapper.PrimitivoMapper;
import com.draftstd.hydraforge.domain.model.mecanica.Primitivo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PrimitivoPersistenceAdapter {

    private final PrimitivoRepository primitivoRepository;
    private final PrimitivoMapper primitivoMapper;

    public PrimitivoPersistenceAdapter(PrimitivoRepository primitivoRepository, PrimitivoMapper primitivoMapper) {
        this.primitivoRepository = primitivoRepository;
        this.primitivoMapper = primitivoMapper;
    }

    public Primitivo salvar(Primitivo primitivo) {
        PrimitivoJpaEntity entity = primitivoMapper.toEntity(primitivo);
        PrimitivoJpaEntity savedEntity = primitivoRepository.save(entity);
        return primitivoMapper.toDomain(savedEntity);
    }

    public Optional<Primitivo> buscarPorId(UUID id) {
        return primitivoRepository.findById(id).map(primitivoMapper::toDomain);
    }

    public List<Primitivo> listarTodos() {
        return primitivoRepository.findAll().stream()
                .map(primitivoMapper::toDomain)
                .collect(Collectors.toList());
    }

    public void deletar(UUID id) {
        primitivoRepository.deleteById(id);
    }
}