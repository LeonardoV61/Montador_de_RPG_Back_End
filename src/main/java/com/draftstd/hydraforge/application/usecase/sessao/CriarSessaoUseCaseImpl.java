package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.domain.events.sessao.SessaoCriadaEvent;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.command.sessao.CriarSessaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.CriarSessaoUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarSessaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CriarSessaoUseCaseImpl implements CriarSessaoUseCase {

    private final SalvarSessaoPort salvarSessaoPort;
    private final EventStorePort eventStorePort;

    public CriarSessaoUseCaseImpl(SalvarSessaoPort salvarSessaoPort, EventStorePort eventStorePort) {
        this.salvarSessaoPort = salvarSessaoPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    @Transactional
    public SessaoQueryResponse executar(CriarSessaoCommand command) {
        Sessao sessao = new Sessao();
        // O id da sessão é gerado automaticamente por sequence (Long), não se usa setId com UUID[cite: 17]

        if (command.campanhaId() != null) {
            Campanha campanha = new Campanha();
            sessao.setCampanha(campanha);
        }

        sessao.setStatus(StatusSessao.AGENDADA);
        sessao.setOrdem(1);

        Sessao salva = salvarSessaoPort.salvar(sessao);

        // Conversão segura do ID Long para UUID para o response e eventos
        UUID sessaoUuid = salva.getId() != null 
                ? UUID.nameUUIDFromBytes(salva.getId().toString().getBytes()) 
                : UUID.randomUUID();

        UUID campanhaId = command.campanhaId();

        long versao = 1L;
        String sessaoIdStr = sessaoUuid.toString();

        SessaoCriadaEvent evento = new SessaoCriadaEvent(
                UUID.randomUUID(),    // eventId (UUID)
                sessaoIdStr,          // aggregateId (String)
                sessaoUuid,           // sessaoId (UUID)
                campanhaId,           // campanhaId (UUID)
                null,                 // titulo (String)
                versao,               // version (long)
                Instant.now()         // occurredOn (Instant)
        );

        eventStorePort.saveEvents(sessaoIdStr, List.of(evento), versao - 1);

        return new SessaoQueryResponse(
                sessaoUuid,
                campanhaId,
                salva.getStatus(),
                List.of()
        );
    }
}