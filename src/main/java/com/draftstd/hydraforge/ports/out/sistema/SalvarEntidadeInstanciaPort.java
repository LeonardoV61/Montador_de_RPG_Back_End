package com.draftstd.hydraforge.ports.out.sistema;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;

public interface SalvarEntidadeInstanciaPort {
    EntidadeInstancia salvar(EntidadeInstancia instancia);
}