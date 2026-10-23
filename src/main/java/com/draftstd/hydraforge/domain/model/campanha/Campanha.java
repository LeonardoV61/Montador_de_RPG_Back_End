package com.draftstd.hydraforge.domain.model.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import com.draftstd.hydraforge.domain.enums.StatusMembroCampanha;
import com.draftstd.hydraforge.domain.events.campanha.CampanhaCriadaEvent;
import com.draftstd.hydraforge.domain.events.campanha.MembroCampanhaAdicionadoEvent;
import com.draftstd.hydraforge.domain.events.campanha.MembroCampanhaRemovidoEvent;
import com.draftstd.hydraforge.domain.events.campanha.StatusCampanhaAlteradoEvent;
import com.draftstd.hydraforge.domain.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Campanha {

    private final UUID id;
    private String nome;
    private String descricao;
    private String urlImagem;
    private StatusCampanha status;
    private final UUID sistemaId;
    private final UUID mestreId;
    private final LocalDateTime criadaEm;
    private LocalDateTime atualizadaEm;
    private long version = 0L;

    private final List<CampanhaMembro> membros = new ArrayList<>();
    private final List<UUID> sessoesIds = new ArrayList<>();
    private final List<UUID> personagensIds = new ArrayList<>();
    private final List<UUID> instanciasIds = new ArrayList<>();

    // Coleção transiente para guardar os eventos pendentes de gravação
    private final transient List<DomainEvent> eventosPendentes = new ArrayList<>();

    // Construtor de criação
    public Campanha(UUID id, String nome, String descricao, String urlImagem, UUID sistemaId, UUID mestreId) {
        this.id = Objects.requireNonNull(id, "O ID da campanha é obrigatório");
        this.nome = Objects.requireNonNull(nome, "O nome da campanha é obrigatório");
        this.descricao = descricao;
        this.urlImagem = urlImagem;
        this.sistemaId = Objects.requireNonNull(sistemaId, "O ID do sistema é obrigatório");
        this.mestreId = Objects.requireNonNull(mestreId, "O ID do mestre é obrigatório");
        this.status = StatusCampanha.EM_PREPARACAO;
        this.criadaEm = LocalDateTime.now();
        this.atualizadaEm = this.criadaEm;
        
        // Regista o evento de criação da campanha
        registrarEvento(new CampanhaCriadaEvent(this.id, this.sistemaId, this.mestreId, this.nome, this.version, this.id));

        // Mestre entra como primeiro membro ativo com o papel de MESTRE
        adicionarMembro(mestreId, PapeisUsuario.MESTRE, StatusMembroCampanha.ATIVO);
    }

    // Construtor de reidratação
    public Campanha(UUID id, String nome, String descricao, String urlImagem, StatusCampanha status, 
                    UUID sistemaId, UUID mestreId, LocalDateTime criadaEm, LocalDateTime atualizadaEm,
                    List<CampanhaMembro> membros, List<UUID> sessoesIds, 
                    List<UUID> personagensIds, List<UUID> instanciasIds, long version) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.urlImagem = urlImagem;
        this.status = status;
        this.sistemaId = sistemaId;
        this.mestreId = mestreId;
        this.criadaEm = criadaEm;
        this.atualizadaEm = atualizadaEm;
        this.version = version;
        if (membros != null) this.membros.addAll(membros);
        if (sessoesIds != null) this.sessoesIds.addAll(sessoesIds);
        if (personagensIds != null) this.personagensIds.addAll(personagensIds);
        if (instanciasIds != null) this.instanciasIds.addAll(instanciasIds);
    }

    // Métodos de Gestão de Eventos (Event Sourcing)
    public List<DomainEvent> pullEventos() {
        List<DomainEvent> eventos = new ArrayList<>(this.eventosPendentes);
        this.eventosPendentes.clear();
        return Collections.unmodifiableList(eventos);
    }

    private void registrarEvento(DomainEvent evento) {
        this.eventosPendentes.add(evento);
    }

    // Métodos de Negócio
    public void atualizarInformacoes(String nome, String descricao, String urlImagem) {
        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
        }
        this.descricao = descricao;
        this.urlImagem = urlImagem;
        this.atualizadaEm = LocalDateTime.now();
    }

    public void alterarStatus(StatusCampanha novoStatus) {
        StatusCampanha statusAnterior = this.status;
        this.status = Objects.requireNonNull(novoStatus, "O novo status não pode ser nulo");
        this.atualizadaEm = LocalDateTime.now();
        registrarEvento(new StatusCampanhaAlteradoEvent(this.id, statusAnterior, novoStatus, this.version, this.id));
    }

    public void adicionarMembro(UUID usuarioId, PapeisUsuario papel, StatusMembroCampanha statusMembro) {
        boolean jaExiste = membros.stream().anyMatch(m -> m.usuarioId().equals(usuarioId));
        if (!jaExiste) {
            membros.add(new CampanhaMembro(usuarioId, papel, statusMembro, LocalDateTime.now()));
            this.atualizadaEm = LocalDateTime.now();
            registrarEvento(new MembroCampanhaAdicionadoEvent(this.id, usuarioId, papel, statusMembro, this.version, this.id));
        }
    }

    public void alterarStatusMembro(UUID usuarioId, StatusMembroCampanha novoStatus) {
        if (usuarioId.equals(mestreId) && novoStatus != StatusMembroCampanha.ATIVO) {
            throw new IllegalArgumentException("O status do mestre da campanha não pode ser alterado para inativo");
        }
        
        membros.stream()
            .filter(m -> m.usuarioId().equals(usuarioId))
            .findFirst()
            .ifPresent(membroAntigo -> {
                membros.remove(membroAntigo);
                membros.add(new CampanhaMembro(membroAntigo.usuarioId(), membroAntigo.papel(), novoStatus, membroAntigo.entrouEm()));
                this.atualizadaEm = LocalDateTime.now();
                registrarEvento(new MembroCampanhaAdicionadoEvent(this.id, usuarioId, membroAntigo.papel(), novoStatus, this.version, this.id));
            });
    }

    public void removerMembro(UUID usuarioId) {
        if (usuarioId.equals(mestreId)) {
            throw new IllegalArgumentException("O mestre não pode ser removido da campanha");
        }
        membros.removeIf(m -> m.usuarioId().equals(usuarioId));
        this.atualizadaEm = LocalDateTime.now();
        registrarEvento(new MembroCampanhaRemovidoEvent(this.id, usuarioId, this.version, this.id));
    }

    public void vincularSessao(UUID sessaoId) {
        if (!sessoesIds.contains(sessaoId)) {
            sessoesIds.add(sessaoId);
            this.atualizadaEm = LocalDateTime.now();
        }
    }

    public void vincularPersonagem(UUID personagemId) {
        if (!personagensIds.contains(personagemId)) {
            personagensIds.add(personagemId);
            this.atualizadaEm = LocalDateTime.now();
        }
    }

    public void vincularEntidadeInstancia(UUID instanciaId) {
        if (!instanciasIds.contains(instanciaId)) {
            instanciasIds.add(instanciaId);
            this.atualizadaEm = LocalDateTime.now();
        }
    }

    // Getters
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getUrlImagem() { return urlImagem; }
    public StatusCampanha getStatus() { return status; }
    public UUID getSistemaId() { return sistemaId; }
    public UUID getMestreId() { return mestreId; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public LocalDateTime getAtualizadaEm() { return atualizadaEm; }
    public List<CampanhaMembro> getMembros() { return Collections.unmodifiableList(membros); }
    public List<UUID> getSessoesIds() { return Collections.unmodifiableList(sessoesIds); }
    public List<UUID> getPersonagensIds() { return Collections.unmodifiableList(personagensIds); }
    public List<UUID> getInstanciasIds() { return Collections.unmodifiableList(instanciasIds); }
    public long getVersion() { return version; }
    public void incrementVersion() { this.version++; }
}