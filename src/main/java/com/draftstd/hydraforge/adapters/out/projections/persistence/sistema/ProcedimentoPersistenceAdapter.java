package com.draftstd.hydraforge.adapters.out.projections.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ProcedimentoOutputPort;
import com.draftstd.hydraforge.adapters.out.repository.sistema.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProcedimentoPersistenceAdapter implements ProcedimentoOutputPort {

    private final ProcedimentoRepository procedimentoRepository;

    @Override
    public Procedimento salvar(Procedimento procedimento) {
        return procedimentoRepository.save(procedimento);
    }

    @Override
    public Optional<Procedimento> buscarPorId(UUID id) {
        return procedimentoRepository.findById(id);
    }

    @Override
    public List<Procedimento> listarPorSistema(UUID sistemaId) {
        return procedimentoRepository.findBySistemaId(sistemaId);
    }

    @Override
    public List<Procedimento> listarTodos() {
        return procedimentoRepository.findAll();
    }

    @Override
    public void deletarPorId(UUID id) {
        procedimentoRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return procedimentoRepository.existsById(id);
    }
}