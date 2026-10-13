package com.draftstd.hydraforge.adapters.out.projections.persistence.entidade;

import com.draftstd.hydraforge.adapters.out.repository.entidade.EntidadeInstanciaRepository;
import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.ports.out.persistence.entidade.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.ports.out.sistema.SalvarEntidadeInstanciaPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EntidadeInstanciaPersistenceAdapter implements SalvarEntidadeInstanciaPort, EntidadeInstanciaOutputPort {

    private final EntidadeInstanciaRepository entidadeInstanciaRepository;

    public EntidadeInstanciaPersistenceAdapter(EntidadeInstanciaRepository entidadeInstanciaRepository) {
        this.entidadeInstanciaRepository = entidadeInstanciaRepository;
    }

    @Override
    public EntidadeInstancia salvar(EntidadeInstancia entidadeInstancia) {
        return entidadeInstanciaRepository.save(entidadeInstancia);
    }

    @Override
    public Optional<EntidadeInstancia> buscarPorId(UUID id) {
        return entidadeInstanciaRepository.findById(id);
    }

    @Override
    public List<EntidadeInstancia> listarPorCampanhaId(UUID campanhaId) {
        return entidadeInstanciaRepository.findAllByCampanhaId(campanhaId);
    }

}