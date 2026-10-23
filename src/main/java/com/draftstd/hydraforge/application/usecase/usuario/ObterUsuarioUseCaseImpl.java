package com.draftstd.hydraforge.application.usecase.usuario;

import com.draftstd.hydraforge.application.response.usuario.ObterUsuarioQuery;
import com.draftstd.hydraforge.application.response.usuario.UsuarioQueryResponse;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import com.draftstd.hydraforge.ports.in.usuario.ObterUsuarioUseCase;
import com.draftstd.hydraforge.ports.out.persistence.usuario.ObterUsuarioPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ObterUsuarioUseCaseImpl implements ObterUsuarioUseCase {

    private final ObterUsuarioPort obterUsuarioPort;

    public ObterUsuarioUseCaseImpl(ObterUsuarioPort obterUsuarioPort) {
        this.obterUsuarioPort = obterUsuarioPort;
    }

    @Override
    public UsuarioQueryResponse obterPorId(ObterUsuarioQuery query) {
        Usuario usuario = obterUsuarioPort.buscarPorId(query.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + query.id()));

        return new UsuarioQueryResponse(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getApelido(),
                usuario.getUrlImagem(),
                usuario.isEAdmin(),
                usuario.getProvider(),
                usuario.getCriadoEm()
        );
    }
}