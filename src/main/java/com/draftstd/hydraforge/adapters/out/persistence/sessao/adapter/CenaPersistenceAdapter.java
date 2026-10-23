package com.draftstd.hydraforge.adapters.out.persistence.sessao.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.CenaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper.CenaMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.repository.CenaRepository;
import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CenaPersistenceAdapter implements CenaPersistencePort {

    private final CenaRepository cenaRepository;
    private final CenaMapper cenaMapper;

    public CenaPersistenceAdapter(CenaRepository cenaRepository, CenaMapper cenaMapper) {
        this.cenaRepository = cenaRepository;
        this.cenaMapper = cenaMapper;
    }

    @Override
    public void salvar(Cena cena) {
        CenaJpaEntity entity = cenaMapper.toEntity(cena);
        cenaRepository.save(entity);
    }

    @Override
    public Optional<Cena> buscarPorId(UUID id) {
        return cenaRepository.findById(id).map(cenaMapper::toDomain);
    }

    @Override
    public Optional<Cena> buscarUltimaPorSessaoId(UUID sessaoId) {
        return cenaRepository.findTopBySessao_IdOrderByOrdemDesc(sessaoId)
                .map(cenaMapper::toDomain);
    }

    @Override
    public List<Cena> buscarPorSessaoIdOrdenadoPorOrdemDesc(UUID sessaoId) {
        return cenaRepository.findBySessao_IdOrderByOrdemDesc(sessaoId)
                .stream()
                .map(cenaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long contarPorCriadorIdEPapel(UUID usuarioId, PapeisUsuario papel) {
        return cenaRepository.countByCriadorId(usuarioId, papel);
    }

    @Override
    public long contarPorCriadorIdEPapelEComCamposNulos(UUID usuarioId, PapeisUsuario papel) {
        return cenaRepository.countByCriadorIdAndCamposNulos(usuarioId, papel);
    }

    @Override
    public void deletarPorId(UUID id) {
        cenaRepository.deleteById(id);
    }
}