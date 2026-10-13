package com.draftstd.hydraforge.adapters.out.projections.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EtapaProcedimentoOutputPort;
import com.draftstd.hydraforge.adapters.out.repository.sistema.EtapaProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EtapaProcedimentoPersistenceAdapter implements EtapaProcedimentoOutputPort {

    private final EtapaProcedimentoRepository etapaRepository;

    @Override
    public EtapaProcedimento salvar(EtapaProcedimento etapa) {
        return etapaRepository.save(etapa);
    }

    @Override
    public Optional<EtapaProcedimento> buscarPorId(UUID id) {
        return etapaRepository.findById(id);
    }

    @Override
    public List<EtapaProcedimento> buscarPorProcedimentoIdOrdenado(UUID procedimentoId) {
        return etapaRepository.findByProcedimentoIdOrderByOrdem(procedimentoId);
    }

    @Override
    public boolean existePorProcedimentoEOrdem(UUID procedimentoId, Integer ordem) {
        return etapaRepository.existsByProcedimentoIdAndOrdem(procedimentoId, ordem);
    }

    @Override
    public void deletarPorId(UUID id) {
        etapaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return etapaRepository.existsById(id);
    }
}