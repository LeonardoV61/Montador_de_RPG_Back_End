package com.draftstd.hydraforge.ports.out.security;

import com.draftstd.hydraforge.domain.model.usuario.Usuario;

public interface GerarTokenPort {
    String gerarToken(Usuario usuario);
    String gerarTokenPorEmail(String email);
}