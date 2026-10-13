package com.draftstd.hydraforge.application.usecase.personagem;

import com.draftstd.hydraforge.domain.events.personagem.*;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.ports.in.command.personagem.*;
import com.draftstd.hydraforge.ports.in.personagem.*;
import com.draftstd.hydraforge.ports.in.query.personagem.*;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.personagem.SalvarPersonagemPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class CriarPersonagemUseCaseImpl implements CriarPersonagemUseCase {

    private final SalvarPersonagemPort salvarPersonagemPort;
    private final EventStorePort eventStorePort;

    public CriarPersonagemUseCaseImpl(SalvarPersonagemPort salvarPersonagemPort, EventStorePort eventStorePort) {
        this.salvarPersonagemPort = salvarPersonagemPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    public PersonagemQueryResponse executar(CriarPersonagemCommand command) {
        Personagem p = new Personagem();
        // O ID é gerado automaticamente por sequence (Long), por isso não chamamos setId com UUID.

        // Se houver necessidade de associar o usuário ou campanha, instanciamos ou buscamos as referências:
        // Usuario usuario = new Usuario();
        // usuario.setId(command.usuarioId());
        // p.setUsuario(usuario);

        if (command.campanhaId() != null) {
            Campanha campanha = new Campanha();
            // Ajuste conforme o tipo do ID da campanha se necessário
            p.setCampanha(campanha);
        }

        // Mapeamos a história ou conceito se aplicável
        // p.setHistoria(command.conceito());

        Personagem salvo = salvarPersonagemPort.salvar(p);

        long versao = 1L;
        String salvoIdStr = salvo.getId() != null ? salvo.getId().toString() : UUID.randomUUID().toString();

        // Conversão do ID para UUID para o evento e response
        UUID personagemUuid = salvo.getId() != null ? UUID.nameUUIDFromBytes(salvo.getId().toString().getBytes()) : UUID.randomUUID();

        UUID usuarioId = command.usuarioId();
        UUID sistemaId = null;
        UUID campanhaId = command.campanhaId();

        Map<String, Object> atributosMap = Map.of();

        PersonagemCriadoEvent evento = new PersonagemCriadoEvent(UUID.randomUUID(), salvoIdStr, usuarioId, campanhaId, command.nome(), atributosMap, versao, Instant.now());
        eventStorePort.saveEvents(salvoIdStr, List.of(evento), 0L);

        return new PersonagemQueryResponse(personagemUuid, usuarioId, sistemaId, campanhaId, command.nome(), command.conceito(), command.dadosJson() != null ? command.dadosJson().toString() : null, salvo.getCriadoEm() != null ? salvo.getCriadoEm() : LocalDateTime.now(), LocalDateTime.now());
    }
}