package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.domain.events.sessao.SessaoCriadaEvent;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.command.sessao.CriarSessaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.CriarSessaoUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarSessaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
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
        sessao.setId(UUID.randomUUID());
        sessao.setCampanhaId(command.campanhaId());
        sessao.setTitulo(command.titulo());
        sessao.setDescricao(command.descricao());
        sessao.setStatus(StatusSessao.AGENDADA);
        sessao.setAgendadaPara(command.agendadaPara());
        sessao.setCriadaEm(OffsetDateTime.now());

        Sessao salva = salvarSessaoPort.salvar(sessao);

        SessaoCriadaEvent evento = new SessaoCriadaEvent(
                salva.getId(),
                salva.getCampanhaId(),
                salva.getTitulo(),
                salva.getAgendadaPara(),
                salva.getCriadaEm()
        );

        eventStorePort.saveEvents(salva.getId().toString(), List.of(evento), 0L);

        return new SessaoQueryResponse(
                salva.getId(),
                salva.getCampanhaId(),
                salva.getTitulo(),
                salva.getDescricao(),
                salva.getStatus(),
                salva.getAgendadaPara(),
                salva.getCriadaEm()
        );
    }
}