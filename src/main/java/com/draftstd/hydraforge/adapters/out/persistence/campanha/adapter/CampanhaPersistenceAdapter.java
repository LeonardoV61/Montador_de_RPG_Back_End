package com.draftstd.hydraforge.adapters.out.persistence.campanha.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaMembroJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaMembroKeyJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.campanha.mapper.CampanhaMapper;
import com.draftstd.hydraforge.adapters.out.persistence.campanha.repository.CampanhaMembroRepository;
import com.draftstd.hydraforge.adapters.out.persistence.campanha.repository.CampanhaRepository;
import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.domain.model.campanha.CampanhaMembro;
import com.draftstd.hydraforge.ports.out.persistence.campanha.CampanhaPersistencePort;
import com.draftstd.hydraforge.ports.out.persistence.campanha.GerenciarParticipantesCampanhaPort;
import com.draftstd.hydraforge.ports.out.persistence.campanha.ObterCampanhaMembroPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CampanhaPersistenceAdapter
        implements CampanhaPersistencePort, GerenciarParticipantesCampanhaPort, ObterCampanhaMembroPort {

    private final CampanhaRepository campanhaRepository;
    private final CampanhaMembroRepository campanhaMembroRepository;
    private final CampanhaMapper mapper;

    public CampanhaPersistenceAdapter(
            CampanhaRepository campanhaRepository,
            CampanhaMembroRepository campanhaMembroRepository,
            CampanhaMapper mapper
    ) {
        this.campanhaRepository = campanhaRepository;
        this.campanhaMembroRepository = campanhaMembroRepository;
        this.mapper = mapper;
    }

    @Override
    public void salvar(Campanha campanha) {
        var entity = mapper.toEntity(campanha);
        campanhaRepository.save(entity);
    }

    @Override
    public void deletar(UUID id) {
        campanhaRepository.deleteById(id);
    }

    @Override
    public Optional<Campanha> buscarPorId(UUID id) {
        return campanhaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Campanha> listarTodas() {
        return campanhaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Campanha> listarPorUsuario(UUID usuarioId) {
        return campanhaRepository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void salvarParticipante(UUID campanhaId, CampanhaMembro membro) {
        CampanhaMembroJpaEntity entity = new CampanhaMembroJpaEntity();
        entity.setId(new CampanhaMembroKeyJpaEntity(campanhaId, membro.usuarioId()));
        entity.setPapel(membro.papel());
        entity.setStatus(membro.status());
        entity.setEntrouEm(membro.entrouEm());
        campanhaMembroRepository.save(entity);
    }

    @Override
    public void removerParticipante(UUID campanhaId, UUID usuarioId) {
        campanhaMembroRepository.deleteById(new CampanhaMembroKeyJpaEntity(campanhaId, usuarioId));
    }

    @Override
    public Optional<CampanhaMembro> buscarParticipante(UUID campanhaId, UUID usuarioId) {
        return campanhaMembroRepository.findByCampanhaIdAndUsuarioId(campanhaId, usuarioId)
                .map(mapper::toDomainParticipante);
    }

    @Override
    public List<CampanhaMembro> listarParticipantesPorCampanha(UUID campanhaId) {
        return campanhaMembroRepository.findByCampanhaId(campanhaId).stream()
                .map(mapper::toDomainParticipante)
                .toList();
    }

    @Override
    public Optional<CampanhaMembro> buscarPorCampanhaEUsuario(UUID idCampanha, UUID idUsuario) {
        return buscarParticipante(idCampanha, idUsuario);
    }

    @Override
    public Optional<CampanhaMembro> buscarPorCampanhaEUsuarioEPapel(UUID idCampanha, UUID idUsuario,
                                                                    PapeisUsuario papel) {
        return buscarParticipante(idCampanha, idUsuario)
                .filter(m -> m.papel() == papel);
    }
}