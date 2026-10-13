package com.draftstd.hydraforge.ports.out.persistence.entidade;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntidadeInstanciaOutputPort {
    EntidadeInstancia salvar(EntidadeInstancia entidadeInstancia);
    Optional<EntidadeInstancia> buscarPorId(UUID id);
    List<EntidadeInstancia> listarPorCampanhaId(UUID campanhaId);
}