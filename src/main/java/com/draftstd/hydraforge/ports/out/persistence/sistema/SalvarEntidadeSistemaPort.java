package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeSistema;

public interface SalvarEntidadeSistemaPort {
    void salvar(EntidadeSistema entidadeSistema);
}