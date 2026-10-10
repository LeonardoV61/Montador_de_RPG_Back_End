package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.personagem.ObterPersonagemUseCase;
import com.draftstd.hydraforge.ports.in.query.personagem.ListarPersonagensPorUsuarioQuery;
import com.draftstd.hydraforge.ports.in.query.personagem.ObterPersonagemQuery;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.ports.out.personagem.ObterPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ObterPersonagemUseCaseImpl implements ObterPersonagemUseCase {

    private final ObterPersonagemPort obterPersonagemPort;

    public ObterPersonagemUseCaseImpl(ObterPersonagemPort obterPersonagemPort) {
        this.obterPersonagemPort = obterPersonagemPort;
    }

    @Override
    public PersonagemQueryResponse obterPorId(ObterPersonagemQuery query) {
        Personagem personagem = obterPersonagemPort.buscarPorId(query.personagemId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + query.personagemId()));

        return toQueryResponse(personagem);
    }

    @Override
    public List<PersonagemQueryResponse> listarPorUsuario(ListarPersonagensPorUsuarioQuery query) {
        return obterPersonagemPort.listarPorUsuarioId(query.usuarioId())
                .stream()
                .map(this::toQueryResponse)
                .toList();
    }

    private PersonagemQueryResponse toQueryResponse(Personagem p) {
        return new PersonagemQueryResponse(
                p.getId(),
                p.getUsuarioId(),
                p.getCampanhaId(),
                p.getNome(),
                p.getAtributos()
        );
    }
}