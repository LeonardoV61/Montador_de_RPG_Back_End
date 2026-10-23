package com.draftstd.hydraforge.domain.model.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.domain.events.sessao.SessaoCriadaEvent;
import com.draftstd.hydraforge.domain.events.sessao.StatusSessaoAlteradoEvent;
import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.model.AggregateRoot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Sessao extends AggregateRoot<UUID> {

    private String titulo;
    private UUID campanhaId;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String sessaoSnapshotJson;
    private StatusSessao status;
    private int ordem;
    private UUID procedimentoAtivoId;

    private final List<Cena> cenas = new ArrayList<>();
    private final List<HistoricoAcoes> historicoAcoes = new ArrayList<>();
    private final List<MensagemLog> mensagens = new ArrayList<>();
    private final List<EfeitoAtivo> efeitosAtivos = new ArrayList<>();

    // Construtor vazio para reidratação por Event Sourcing
    public Sessao() {}

    // Construtor de Negócio (Comando de Criação)
    public Sessao(UUID id, String titulo, UUID campanhaId, int ordem) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título da sessão é obrigatório.");
        }
        
        setId(Objects.requireNonNull(id, "O ID da sessão é obrigatório")); // <--- Garantir ID na base
        
        SessaoCriadaEvent event = new SessaoCriadaEvent(
            UUID.randomUUID(),
            id.toString(),
            id,
            Objects.requireNonNull(campanhaId, "O ID da campanha é obrigatório"),
            titulo,
            ordem,
            1L,
            Instant.now()
        );
        raiseEvent(event);
    }

    // Regras de Negócio
    public void iniciarSessao() {
        if (this.status == StatusSessao.ATIVA) {
            throw new IllegalStateException("A sessão já está em andamento.");
        }
        
        long proximaVersao = getVersion() + 1;
        StatusSessaoAlteradoEvent event = new StatusSessaoAlteradoEvent(
            UUID.randomUUID(),
            getId().toString(),
            getId(),
            StatusSessao.ATIVA,
            proximaVersao,
            Instant.now()
        );
        raiseEvent(event);
    }

    public void pausarSessao() {
        if (this.status != StatusSessao.ATIVA) {
            throw new IllegalStateException("Apenas sessões ativas podem ser pausadas.");
        }

        long proximaVersao = getVersion() + 1;
        StatusSessaoAlteradoEvent event = new StatusSessaoAlteradoEvent(
            UUID.randomUUID(),
            getId().toString(),
            getId(),
            StatusSessao.PAUSADA,
            proximaVersao,
            Instant.now()
        );
        raiseEvent(event);
    }

    public void encerrarSessao(String snapshotFinal) {
        if (this.status == StatusSessao.ENCERRADA) {
            throw new IllegalStateException("A sessão já foi encerrada.");
        }

        this.sessaoSnapshotJson = snapshotFinal;
        this.dataFim = LocalDateTime.now();

        long proximaVersao = getVersion() + 1;
        StatusSessaoAlteradoEvent event = new StatusSessaoAlteradoEvent(
            UUID.randomUUID(),
            getId().toString(),
            getId(),
            StatusSessao.ENCERRADA,
            proximaVersao,
            Instant.now()
        );
        raiseEvent(event);
    }

    public void definirProcedimentoAtivo(UUID procedimentoId) {
        this.procedimentoAtivoId = procedimentoId;
    }

    public void adicionarCena(Cena cena) {
        if (cena != null) {
            this.cenas.add(cena);
        }
    }

    public void registrarAcao(UUID instanciaId, String resultadoJson) {
        this.historicoAcoes.add(new HistoricoAcoes(UUID.randomUUID(), instanciaId, LocalDateTime.now(), resultadoJson));
    }

    public void adicionarMensagem(UUID usuarioId, String conteudo) {
        this.mensagens.add(new MensagemLog(UUID.randomUUID(), usuarioId, conteudo, LocalDateTime.now()));
    }

    public void adicionarEfeitoAtivo(EfeitoAtivo efeito) {
        if (efeito != null) {
            this.efeitosAtivos.add(efeito);
        }
    }

    // Aplicação de Eventos para Reidratação / Event Sourcing
    @Override
    protected void apply(DomainEvent event) {
        if (event instanceof SessaoCriadaEvent e) {
            setId(UUID.fromString(e.getAggregateId()));
            this.titulo = e.titulo();
            this.campanhaId = e.campanhaId();
            this.ordem = e.ordem();
            this.status = StatusSessao.AGENDADA;
            this.dataInicio = LocalDateTime.now();
        } else if (event instanceof StatusSessaoAlteradoEvent e) {
            this.status = e.novoStatus();
        }
    }

    // Getters de Leitura
    public String getTitulo() { return titulo; }
    public UUID getCampanhaId() { return campanhaId; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public String getSessaoSnapshotJson() { return sessaoSnapshotJson; }
    public StatusSessao getStatus() { return status; }
    public int getOrdem() { return ordem; }
    public UUID getProcedimentoAtivoId() { return procedimentoAtivoId; }
    public List<Cena> getCenas() { return Collections.unmodifiableList(cenas); }
    public List<HistoricoAcoes> getHistoricoAcoes() { return Collections.unmodifiableList(historicoAcoes); }
    public List<MensagemLog> getMensagens() { return Collections.unmodifiableList(mensagens); }
    public List<EfeitoAtivo> getEfeitosAtivos() { return Collections.unmodifiableList(efeitosAtivos); }
}