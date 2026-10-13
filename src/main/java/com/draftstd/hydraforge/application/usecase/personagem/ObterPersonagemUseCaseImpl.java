package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.personagem.ObterPersonagemUseCase;
import com.draftstd.hydraforge.ports.in.query.personagem.ObterPersonagemQuery;
import com.draftstd.hydraforge.ports.in.query.personagem.ListarPersonagensPorUsuarioQuery;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.ports.out.personagem.ObterPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ObterPersonagemUseCaseImpl implements ObterPersonagemUseCase {

    private final ObterPersonagemPort obterPersonagemPort;

    public ObterPersonagemUseCaseImpl(ObterPersonagemPort obterPersonagemPort) {
        this.obterPersonagemPort = obterPersonagemPort;
    }

    @Override
    public PersonagemQueryResponse obterPorId(ObterPersonagemQuery query) {
        Personagem p = obterPersonagemPort.buscarPorId(query.personagemId()).orElseThrow(() -> new RecursoNaoEncontradoException("Personagem não encontrado: " + query.personagemId()));
        
        return mapToResponse(p);
    }

    @Override
    public List<PersonagemQueryResponse> listarPorUsuario(ListarPersonagensPorUsuarioQuery query) {
        return obterPersonagemPort.listarPorUsuarioId(query.usuarioId()).stream().map(this::mapToResponse).toList();
    }

    private PersonagemQueryResponse mapToResponse(Personagem p) {
        // Conversão segura do ID Long para UUID
        UUID personagemUuid = p.getId() != null ? UUID.nameUUIDFromBytes(p.getId().toString().getBytes()) : UUID.randomUUID();

        // Extração segura de relações
        UUID usuarioId = null; // Ajustar se a entidade Usuario expuser ID UUID
        UUID sistemaId = null;
        UUID campanhaId = (p.getCampanha() != null && p.getCampanha().getId() != null) ? UUID.nameUUIDFromBytes(p.getCampanha().getId().toString().getBytes()) : null;

        return new PersonagemQueryResponse(personagemUuid, usuarioId, sistemaId, campanhaId, null, p.getHistoria(), null, p.getCriadoEm() != null ? p.getCriadoEm() : LocalDateTime.now(), LocalDateTime.now());
    }
}