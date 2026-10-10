package com.draftstd.hydraforge.domain.events.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.events.sessao.SessaoCriadaEvent;
import com.draftstd.hydraforge.domain.events.sessao.StatusSessaoAlteradoEvent;
import com.draftstd.hydraforge.domain.model.AggregateRoot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sessao extends AggregateRoot<UUID> {

    private String nome;
    private UUID campanhaId;
    private UUID mestreId;
    private StatusSessao status;
    private List<UUID> participantesIds = new ArrayList<>();

    // Construtor privado para reconstrução via Event Sourcing
    public Sessao() {}

    // Construtor de Negócio (Comando de Criação)
    public Sessao(UUID id, String nome, UUID campanhaId, UUID mestreId) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da sessão é obrigatório.");
        }
        
        SessaoCriadaEvent event = new SessaoCriadaEvent(id, nome, campanhaId, mestreId);
        raiseEvent(event);
    }

    // Regra de Negócio: Iniciar Sessão
    public void iniciarSessao() {
        if (this.status == StatusSessao.EM_ANDAMENTO) {
            throw new IllegalStateException("A sessão já está em andamento.");
        }
        
        StatusSessaoAlteradoEvent event = new StatusSessaoAlteradoEvent(
            getId(), 
            StatusSessao.EM_ANDAMENTO, 
            getVersion() + 1
        );
        raiseEvent(event);
    }

    // Regra de Negócio: Encerrar Sessão
    public void encerrarSessao() {
        if (this.status == StatusSessao.FINALIZADA) {
            throw new IllegalStateException("A sessão já foi encerrada.");
        }

        StatusSessaoAlteradoEvent event = new StatusSessaoAlteradoEvent(
            getId(), 
            StatusSessao.FINALIZADA, 
            getVersion() + 1
        );
        raiseEvent(event);
    }

    // Aplicação dos eventos para mutar o estado interno (Rehydration / Event Sourcing)
    @Override
    protected void apply(DomainEvent event) {
        if (event instanceof SessaoCriadaEvent e) {
            setId(UUID.fromString(e.aggregateId()));
            this.nome = e.nome();
            this.campanhaId = e.campanhaId();
            this.mestreId = e.mestreId();
            this.status = StatusSessao.PLANEJADA; // ou status inicial padrão
        } else if (event instanceof StatusSessaoAlteradoEvent e) {
            this.status = e.novoStatus();
        }
    }

    // Getters de Leitura do Domínio
    public String getNome() { return nome; }
    public UUID getCampanhaId() { return campanhaId; }
    public UUID getMestreId() { return mestreId; }
    public StatusSessao getStatus() { return status; }
    public List<UUID> getParticipantesIds() { return participantesIds; }
}
