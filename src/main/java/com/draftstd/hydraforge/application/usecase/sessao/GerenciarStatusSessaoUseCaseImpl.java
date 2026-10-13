package com.draftstd.hydraforge.application.usecase.sessao;

import com.draftstd.hydraforge.domain.events.sessao.StatusSessaoAlteradoEvent;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.ports.in.command.sessao.AlterarStatusSessaoCommand;
import com.draftstd.hydraforge.ports.in.query.sessao.SessaoQueryResponse;
import com.draftstd.hydraforge.ports.in.sessao.GerenciarStatusSessaoUseCase;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.draftstd.hydraforge.ports.out.sessao.ObterSessaoPort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarSessaoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class GerenciarStatusSessaoUseCaseImpl implements GerenciarStatusSessaoUseCase {

    private final ObterSessaoPort obterSessaoPort;
    private final SalvarSessaoPort salvarSessaoPort;
    private final EventStorePort eventStorePort;

    public GerenciarStatusSessaoUseCaseImpl(ObterSessaoPort obterSessaoPort, SalvarSessaoPort salvarSessaoPort, EventStorePort eventStorePort) {
        this.obterSessaoPort = obterSessaoPort;
        this.salvarSessaoPort = salvarSessaoPort;
        this.eventStorePort = eventStorePort;
    }

    @Override
    @Transactional
    public SessaoQueryResponse alterarStatus(AlterarStatusSessaoCommand command) {
        Sessao sessao = obterSessaoPort.buscarPorId(command.sessaoId()).orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada: " + command.sessaoId()));

        // Atualização utilizando o setter padrão da entidade
        sessao.setStatus(command.novoStatus());

        Sessao sessaoSalva = salvarSessaoPort.salvar(sessao);

        // Conversão segura do ID Long para UUID
        UUID sessaoUuid = sessaoSalva.getId() != null ? UUID.nameUUIDFromBytes(sessaoSalva.getId().toString().getBytes()) : UUID.randomUUID();

        long versao = 1L;
        String aggregateId = sessaoUuid.toString();

        // Instanciação correta do evento com os 6 parâmetros exigidos pelo record
        StatusSessaoAlteradoEvent event = new StatusSessaoAlteradoEvent(UUID.randomUUID(), aggregateId, sessaoUuid, command.novoStatus(), versao, Instant.now());

        eventStorePort.saveEvents(aggregateId, List.of(event), versao - 1);

        // Extração segura do ID da campanha a partir da relação JPA
        UUID campanhaId = (sessaoSalva.getCampanha() != null && sessaoSalva.getCampanha().getId() != null) ? UUID.nameUUIDFromBytes(sessaoSalva.getCampanha().getId().toString().getBytes()) : null;

        // Retorno compatível com os 4 parâmetros do SessaoQueryResponse
        return new SessaoQueryResponse(sessaoUuid, campanhaId, sessaoSalva.getStatus(), List.of());
    }
}