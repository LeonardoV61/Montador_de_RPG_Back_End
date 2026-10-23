package com.draftstd.hydraforge.ports.in.usuario;

import com.draftstd.hydraforge.application.response.usuario.ObterUsuarioQuery;
import com.draftstd.hydraforge.application.response.usuario.UsuarioQueryResponse;

public interface ObterUsuarioUseCase {
    UsuarioQueryResponse obterPorId(ObterUsuarioQuery query);
}