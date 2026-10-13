package com.draftstd.hydraforge.ports.in.usuario;

import com.draftstd.hydraforge.ports.in.query.usuario.UsuarioQueryResponse;
import java.util.UUID;

public interface ObterUsuarioUseCase {
    UsuarioQueryResponse obterPorId(UUID id);
}