package com.draftstd.hydraforge.adapters.out.projections.persistence.sessao;

import com.draftstd.hydraforge.adapters.out.repository.sessao.AnotacaoRepository;
import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.ports.out.sessao.ObterAnotacaoPort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarAnotacaoPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AnotacaoPersistenceAdapter implements SalvarAnotacaoPort, ObterAnotacaoPort {

    private final AnotacaoRepository anotacaoRepository;

    public AnotacaoPersistenceAdapter(AnotacaoRepository anotacaoRepository) {
        this.anotacaoRepository = anotacaoRepository;
    }

    @Override
    public Anotacao salvar(Anotacao anotacao) {
        return anotacaoRepository.save(anotacao);
    }

    @Override
    public Optional<Anotacao> buscarPorId(UUID id) {
        return anotacaoRepository.findById(id);
    }

    @Override
    public List<Anotacao> listarPorSessaoId(UUID sessaoId) {
        return anotacaoRepository.findAllBySessaoId(sessaoId);
    }
}