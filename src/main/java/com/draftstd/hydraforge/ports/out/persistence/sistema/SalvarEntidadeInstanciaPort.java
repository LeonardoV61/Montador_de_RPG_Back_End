package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;

public interface SalvarEntidadeInstanciaPort {
    void salvar(EntidadeInstancia instancia);
}