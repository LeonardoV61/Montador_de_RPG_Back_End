package com.draftstd.hydraforge.ports.in.usuario;

import com.draftstd.hydraforge.application.request.usuario.EnviarSolicitacaoAmizadeCommand;
import com.draftstd.hydraforge.application.request.usuario.ResponderSolicitacaoAmizadeCommand;
import com.draftstd.hydraforge.application.response.usuario.AmizadeQueryResponse;
import com.draftstd.hydraforge.application.response.usuario.ListarAmigosQuery;

import java.util.List;

public interface GerenciarAmizadeUseCase {
    void enviarSolicitacao(EnviarSolicitacaoAmizadeCommand command);
    void aceitarSolicitacao(ResponderSolicitacaoAmizadeCommand command);
    void recusarOuRemover(ResponderSolicitacaoAmizadeCommand command);
    List<AmizadeQueryResponse> listarAmigos(ListarAmigosQuery query);
}