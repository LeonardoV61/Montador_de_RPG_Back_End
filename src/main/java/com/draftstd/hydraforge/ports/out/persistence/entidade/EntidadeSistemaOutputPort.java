package com.draftstd.hydraforge.ports.out.persistence.entidade;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeSistema;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntidadeSistemaOutputPort {
    EntidadeSistema salvar(EntidadeSistema entidadeSistema);
    Optional<EntidadeSistema> buscarPorId(UUID id);
    List<EntidadeSistema> listarPorSistemaId(UUID sistemaId);
}