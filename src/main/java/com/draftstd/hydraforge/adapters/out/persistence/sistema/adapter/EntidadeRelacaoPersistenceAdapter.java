package com.draftstd.hydraforge.adapters.out.persistence.sistema.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeInstanciaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeRelacaoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeSistemaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper.EntidadeRelacaoMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.EntidadeInstanciaRepository;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.EntidadeRelacaoRepository;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.EntidadeSistemaRepository;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeRelacao;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeRelacaoOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EntidadeRelacaoPersistenceAdapter implements EntidadeRelacaoOutputPort {

    private final EntidadeRelacaoRepository entidadeRelacaoRepository;
    private final EntidadeInstanciaRepository entidadeInstanciaRepository;
    private final EntidadeSistemaRepository entidadeSistemaRepository;
    private final EntidadeRelacaoMapper mapper;

    public EntidadeRelacaoPersistenceAdapter(EntidadeRelacaoRepository entidadeRelacaoRepository,
                                             EntidadeInstanciaRepository entidadeInstanciaRepository,
                                             EntidadeSistemaRepository entidadeSistemaRepository,
                                             EntidadeRelacaoMapper mapper) {
        this.entidadeRelacaoRepository = entidadeRelacaoRepository;
        this.entidadeInstanciaRepository = entidadeInstanciaRepository;
        this.entidadeSistemaRepository = entidadeSistemaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<EntidadeRelacao> buscarPorEntidadePaiId(UUID entidadePaiId) {
        return entidadeRelacaoRepository.findByEntidadePaiId(entidadePaiId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<EntidadeRelacao> buscarPorPaiEFilhaId(UUID entidadePaiId, UUID entidadeFilhaId) {
        return entidadeRelacaoRepository.findByEntidadePaiIdAndEntidadeFilhaId(entidadePaiId, entidadeFilhaId)
                .map(mapper::toDomain);
    }

    @Override
    public void salvar(EntidadeRelacao relacao) {
        EntidadeInstanciaJpaEntity paiJpa = entidadeInstanciaRepository.findById(relacao.getEntidadePaiId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Entidade pai não encontrada: " + relacao.getEntidadePaiId()));

        EntidadeInstanciaJpaEntity filhaJpa = entidadeInstanciaRepository.findById(relacao.getEntidadeFilhaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Entidade filha não encontrada: " + relacao.getEntidadeFilhaId()));

        EntidadeSistemaJpaEntity sistemaFilhaJpa = null;
        if (relacao.getEntidadeSistemaFilhaId() != null) {
            sistemaFilhaJpa = entidadeSistemaRepository.findById(relacao.getEntidadeSistemaFilhaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Entidade de sistema filha não encontrada: " + relacao.getEntidadeSistemaFilhaId()));
        }

        EntidadeRelacaoJpaEntity entity = mapper.toEntity(relacao, paiJpa, filhaJpa, sistemaFilhaJpa);
        entidadeRelacaoRepository.save(entity);
    }

    @Override
    public void deletarPorId(UUID id) {
        if (!entidadeRelacaoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Relação não encontrada para exclusão: " + id);
        }
        entidadeRelacaoRepository.deleteById(id);
    }
}