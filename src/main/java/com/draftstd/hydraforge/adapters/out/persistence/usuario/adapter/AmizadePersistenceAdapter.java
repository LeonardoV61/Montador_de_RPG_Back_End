package com.draftstd.hydraforge.adapters.out.persistence.usuario.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.AmizadeKeyEmbeddable;
import com.draftstd.hydraforge.adapters.out.persistence.usuario.mapper.AmizadeMapper;
import com.draftstd.hydraforge.adapters.out.persistence.usuario.repository.AmizadeRepository;
import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import com.draftstd.hydraforge.domain.model.usuario.Amizade;
import com.draftstd.hydraforge.ports.out.persistence.usuario.ObterAmizadePort;
import com.draftstd.hydraforge.ports.out.persistence.usuario.SalvarAmizadePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AmizadePersistenceAdapter implements SalvarAmizadePort, ObterAmizadePort {

    private final AmizadeRepository amizadeRepository;

    public AmizadePersistenceAdapter(AmizadeRepository amizadeRepository) {
        this.amizadeRepository = amizadeRepository;
    }

    @Override
    public void salvar(Amizade amizade) {
        amizadeRepository.save(AmizadeMapper.toEntity(amizade));
    }

    @Override
    public void deletar(UUID idRemetente, UUID idDestinatario) {
        amizadeRepository.deleteById(new AmizadeKeyEmbeddable(idRemetente, idDestinatario));
    }

    @Override
    public Optional<Amizade> buscarEntreUsuarios(UUID idA, UUID idB) {
        return amizadeRepository.findEntreUsuarios(idA, idB)
                .map(AmizadeMapper::toDomain);
    }

    @Override
    public List<Amizade> listarPorUsuarioEStatus(UUID usuarioId, StatusAmizade status) {
        return amizadeRepository.findByUsuarioAndStatus(usuarioId, status).stream()
                .map(AmizadeMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existeEntreUsuarios(UUID idA, UUID idB) {
        return amizadeRepository.findEntreUsuarios(idA, idB).isPresent();
    }
}