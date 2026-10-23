package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.application.response.personagem.ListarPersonagensPorUsuarioQuery;
import com.draftstd.hydraforge.application.response.personagem.ObterPersonagemQuery;
import com.draftstd.hydraforge.application.response.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.personagem.ObterPersonagemUseCase;
import com.draftstd.hydraforge.ports.out.persistence.personagem.ObterPersonagemPort;
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
        Personagem p = obterPersonagemPort.buscarPorId(query.personagemId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + query.personagemId()));
        
        return mapToResponse(p);
    }

    @Override
    public List<PersonagemQueryResponse> listarPorUsuario(ListarPersonagensPorUsuarioQuery query) {
        return obterPersonagemPort.listarPorUsuarioId(query.usuarioId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PersonagemQueryResponse mapToResponse(Personagem p) {
        return new PersonagemQueryResponse(
            p.getId(),
            p.getUsuarioId(),
            p.getCampanhaId(),
            p.getInstanciaId(),
            p.getNome(),
            p.getHistoria(),
            p.isAtivo(),
            p.getAparencia(),
            p.getUrlImagem(),
            p.getNotasJogador(),
            p.getAtributos(),
            p.getCriadoEm()
        );
    }
}