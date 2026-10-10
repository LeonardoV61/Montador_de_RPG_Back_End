package com.draftstd.hydraforge.ports.out.usuario;

import com.draftstd.hydraforge.domain.model.usuario.Usuario;

public interface SalvarUsuarioPort {
    Usuario salvar(Usuario usuario);
}