package com.draftstd.hydraforge.ports.out.persistence.sistema;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeSistema;

public interface EntidadeSistemaOutputPort {
    void salvar(EntidadeSistema entidadeSistema);
    Optional<EntidadeSistema> buscarPorId(UUID id);
    List<EntidadeSistema> listarPorSistemaId(UUID sistemaId);
}