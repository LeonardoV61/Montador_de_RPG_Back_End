package com.draftstd.hydraforge.ports.out.persistence.sistema;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;

public interface EntidadeInstanciaOutputPort {
    void salvar(EntidadeInstancia instancia);
    Optional<EntidadeInstancia> buscarPorId(UUID id);
    List<EntidadeInstancia> buscarPorIds(List<UUID> ids); 
    List<EntidadeInstancia> listarPorCampanhaId(UUID campanhaId);
    void deletarPorId(UUID id);
}