package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.application.response.personagem.ListarPersonagensPorUsuarioQuery;
import com.draftstd.hydraforge.application.response.personagem.ObterPersonagemQuery;
import com.draftstd.hydraforge.application.response.personagem.PersonagemQueryResponse;
import java.util.List;

public interface ObterPersonagemUseCase {
    PersonagemQueryResponse obterPorId(ObterPersonagemQuery query);
    List<PersonagemQueryResponse> listarPorUsuario(ListarPersonagensPorUsuarioQuery query);
}