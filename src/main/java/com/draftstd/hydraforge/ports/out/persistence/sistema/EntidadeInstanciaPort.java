package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;

import java.util.Optional;
import java.util.UUID;

public interface EntidadeInstanciaPort {

    Optional<EntidadeInstancia> buscarPorId(UUID id);

    EntidadeInstancia salvar(EntidadeInstancia instancia);
}