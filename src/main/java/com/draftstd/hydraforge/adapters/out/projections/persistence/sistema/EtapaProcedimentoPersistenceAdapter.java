package com.draftstd.hydraforge.adapters.out.projections.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EtapaProcedimentoOutputPort;
import com.draftstd.hydraforge.adapters.out.repository.sistema.EtapaProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EtapaProcedimentoPersistenceAdapter implements EtapaProcedimentoOutputPort {

    private final EtapaProcedimentoRepository etapaRepository;

    @Override
    public EtapaProcedimento salvar(EtapaProcedimento etapa) {
        return etapaRepository.save(etapa);
    }

    @Override
    public Optional<EtapaProcedimento> buscarPorId(Long id) {
        return etapaRepository.findById(id);
    }

    @Override
    public List<EtapaProcedimento> buscarPorProcedimentoIdOrdenado(Long procedimentoId) {
        return etapaRepository.findByProcedimentoIdOrderByOrdem(procedimentoId);
    }

    @Override
    public boolean existePorProcedimentoEOrdem(Long procedimentoId, Integer ordem) {
        return etapaRepository.existsByProcedimentoIdAndOrdem(procedimentoId, ordem);
    }

    @Override
    public void deletarPorId(Long id) {
        etapaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return etapaRepository.existsById(id);
    }
}