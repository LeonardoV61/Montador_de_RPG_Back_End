package com.draftstd.hydraforge.ports.in.personagem;

import com.draftstd.hydraforge.ports.in.query.personagem.ListarPersonagensPorUsuarioQuery;
import com.draftstd.hydraforge.ports.in.query.personagem.ObterPersonagemQuery;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;
import java.util.List;

public interface ObterPersonagemUseCase {
    PersonagemQueryResponse obterPorId(ObterPersonagemQuery query);
    List<PersonagemQueryResponse> listarPorUsuario(ListarPersonagensPorUsuarioQuery query);
}