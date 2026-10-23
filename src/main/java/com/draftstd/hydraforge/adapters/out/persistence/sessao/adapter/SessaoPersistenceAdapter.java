package com.draftstd.hydraforge.adapters.out.persistence.sessao.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.SessaoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper.SessaoMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.repository.SessaoRepository;
import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.domain.events.sessao.SessaoCriadaEvent;
import com.draftstd.hydraforge.domain.events.sessao.StatusSessaoAlteradoEvent;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.out.persistence.sessao.SessaoPersistencePort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SessaoPersistenceAdapter implements SessaoPersistencePort {

    private final SessaoRepository sessaoRepository;
    private final SessaoMapper sessaoMapper;

    public SessaoPersistenceAdapter(SessaoRepository sessaoRepository, SessaoMapper sessaoMapper) {
        this.sessaoRepository = sessaoRepository;
        this.sessaoMapper = sessaoMapper;
    }

    @Override
    public void salvar(Sessao sessao) {
        SessaoJpaEntity entity = sessaoMapper.toEntity(sessao);
        sessaoRepository.save(entity);
    }

    @Override
    public Optional<Sessao> buscarPorId(UUID id) {
        return sessaoRepository.findById(id)
                .map(sessaoMapper::toDomain);
    }

    @Override
    public List<Sessao> buscarPorStatusEProcedimentoAtivoNaoNulo(StatusSessao status) {
        return sessaoRepository.findByStatusAndProcedimentoAtivoIdIsNotNull(status)
                .stream()
                .map(sessaoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Sessao> buscarAtivaPorCampanhaId(UUID campanhaId) {
        return sessaoRepository.findAtivaByCampanhaId(campanhaId)
                .map(sessaoMapper::toDomain);
    }

    @Override
    public List<Sessao> buscarPorCampanhaId(UUID campanhaId) {
        return sessaoRepository.findByCampanha_Id(campanhaId)
                .stream()
                .map(sessaoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Sessao> buscarPorCampanhaIdOrdenadoPorOrdemDesc(UUID campanhaId) {
        return sessaoRepository.findByCampanha_IdOrderByOrdemDesc(campanhaId)
                .stream()
                .map(sessaoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Sessao> buscarPrimeiraPorCampanhaEStatusOrdenadoPorDataInicioDesc(UUID campanhaId, StatusSessao status) {
        return sessaoRepository.findFirstByCampanha_IdAndStatusOrderByDataInicioDesc(campanhaId, status)
                .map(sessaoMapper::toDomain);
    }

    @Override
    public int contarPorCampanhaId(UUID campanhaId) {
        return sessaoRepository.countByCampanha_Id(campanhaId);
    }

    @Override
    public long contarPorUsuarioIdEMesAtual(UUID usuarioId, int mes, int ano) {
        return sessaoRepository.countByUsuarioIdAndMesAtual(usuarioId, mes, ano);
    }

    @Override
    public void deletarPorId(UUID id) {
        sessaoRepository.deleteById(id);
    }

    @Transactional
    public void criarVisaoLeitura(SessaoCriadaEvent event) {
        SessaoJpaEntity entity = new SessaoJpaEntity();
        entity.setId(event.sessaoId());
        entity.setTitulo(event.titulo());
        entity.setOrdem(event.ordem());
        entity.setStatus(StatusSessao.AGENDADA);
        entity.setDataInicio(LocalDateTime.now());

        if (event.campanhaId() != null) {
            CampanhaJpaEntity campanhaEntity = new CampanhaJpaEntity();
            campanhaEntity.setId(event.campanhaId());
            entity.setCampanha(campanhaEntity);
        }

        sessaoRepository.save(entity);
    }

    @Transactional
    public void atualizarStatusVisao(StatusSessaoAlteradoEvent event) {
        sessaoRepository.findById(event.sessaoId()).ifPresent(entity -> {
            entity.setStatus(event.novoStatus());
            if (event.novoStatus() == StatusSessao.ENCERRADA) {
                entity.setDataFim(LocalDateTime.now());
            }
            sessaoRepository.save(entity);
        });
    }
}