package com.draftstd.hydraforge.ports.out.persistence.usuario;

import com.draftstd.hydraforge.domain.model.usuario.Usuario;

public interface SalvarUsuarioPort {
    void salvar(Usuario usuario);
}