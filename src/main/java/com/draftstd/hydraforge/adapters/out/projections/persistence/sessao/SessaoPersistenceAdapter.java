package com.draftstd.hydraforge.adapters.out.projections.persistence.sessao;

import com.draftstd.hydraforge.adapters.out.repository.sessao.SessaoRepository;
import com.draftstd.hydraforge.domain.events.sessao.SessaoCriadaEvent;
import com.draftstd.hydraforge.domain.events.sessao.StatusSessaoAlteradoEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SessaoPersistenceAdapter {

    private final SessaoRepository sessaoRepository;

    public SessaoPersistenceAdapter(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    @Transactional
    public void criarVisaoLeitura(SessaoCriadaEvent event) {
        // Insere a nova linha na tabela relacional de leitura 'sessao'
        // Ex: sessaoRepository.save(new SessaoEntity(event.sessaoId(), event.nome(), ...));
    }

    @Transactional
    public void atualizarStatusVisao(StatusSessaoAlteradoEvent event) {
        // Atualiza apenas a coluna de status na tabela de leitura
        sessaoRepository.findById(event.sessaoId()).ifPresent(entity -> {
            entity.setStatus(event.novoStatus());
            sessaoRepository.save(entity);
        });
    }
}