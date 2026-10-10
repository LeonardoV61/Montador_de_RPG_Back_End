package com.draftstd.hydraforge.adapters.out.projections.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ProcedimentoOutputPort;
import com.draftstd.hydraforge.adapters.out.repository.sistema.ProcedimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProcedimentoPersistenceAdapter implements ProcedimentoOutputPort {

    private final ProcedimentoRepository procedimentoRepository;

    @Override
    public Procedimento salvar(Procedimento procedimento) {
        return procedimentoRepository.save(procedimento);
    }

    @Override
    public Optional<Procedimento> buscarPorId(Long id) {
        return procedimentoRepository.findById(id);
    }

    @Override
    public List<Procedimento> listarPorSistema(Long sistemaId) {
        return procedimentoRepository.findBySistemaId(sistemaId);
    }

    @Override
    public List<Procedimento> listarTodos() {
        return procedimentoRepository.findAll();
    }

    @Override
    public void deletarPorId(Long id) {
        procedimentoRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return procedimentoRepository.existsById(id);
    }
}