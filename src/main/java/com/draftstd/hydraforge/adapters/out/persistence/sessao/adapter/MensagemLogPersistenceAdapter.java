package com.draftstd.hydraforge.adapters.out.persistence.sessao.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.MensagemLogJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper.MensagemLogMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.repository.MensagemLogRepository;
import com.draftstd.hydraforge.domain.model.sessao.MensagemLog;
import com.draftstd.hydraforge.ports.out.persistence.sessao.MensagemLogPersistencePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MensagemLogPersistenceAdapter implements MensagemLogPersistencePort {

    private final MensagemLogRepository mensagemLogRepository;
    private final MensagemLogMapper mensagemLogMapper;

    public MensagemLogPersistenceAdapter(MensagemLogRepository mensagemLogRepository, MensagemLogMapper mensagemLogMapper) {
        this.mensagemLogRepository = mensagemLogRepository;
        this.mensagemLogMapper = mensagemLogMapper;
    }

    @Override
    public MensagemLog salvar(UUID sessaoId, MensagemLog mensagem) {
        MensagemLogJpaEntity entity = mensagemLogMapper.toEntity(mensagem, sessaoId);
        MensagemLogJpaEntity saved = mensagemLogRepository.save(entity);
        return mensagemLogMapper.toDomain(saved);
    }

    @Override
    public Optional<MensagemLog> buscarPorId(UUID id) {
        return mensagemLogRepository.findById(id).map(mensagemLogMapper::toDomain);
    }

    @Override
    public List<MensagemLog> listarPorSessaoId(UUID sessaoId) {
        return mensagemLogRepository.findBySessao_Id(sessaoId)
                .stream()
                .map(mensagemLogMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MensagemLog> listarTodas() {
        return mensagemLogRepository.findAll()
                .stream()
                .map(mensagemLogMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deletarPorId(UUID id) {
        mensagemLogRepository.deleteById(id);
    }
}