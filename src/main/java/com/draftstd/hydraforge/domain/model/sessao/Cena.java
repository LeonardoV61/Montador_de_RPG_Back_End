package com.draftstd.hydraforge.domain.model.sessao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Cena {

    private final UUID id;
    private final UUID sessaoId;
    private String titulo;
    private String mapaJson;
    private String urlMapa;
    private Integer ordem;
    private String tipo;
    private String estadoJson;
    private Boolean ativa;
    private final LocalDateTime criadaEm;
    private final List<CenaParticipante> participantes = new ArrayList<>();

    // Construtor utilitário para CRIAÇÃO INICIAL (sem nulos)
    public Cena(UUID id, UUID sessaoId, String mapaJson, String urlMapa, Integer ordem, String tipo, String estadoJson) {
        this.id = Objects.requireNonNull(id, "O ID da cena é obrigatório");
        this.sessaoId = Objects.requireNonNull(sessaoId, "O ID da sessão é obrigatório");
        this.ordem = ordem != null ? ordem : 1;
        this.titulo = "Cena " + this.ordem;
        this.mapaJson = mapaJson != null ? mapaJson : "{}";
        this.urlMapa = urlMapa != null ? urlMapa : "";
        this.tipo = tipo != null ? tipo : "PADRAO";
        this.estadoJson = estadoJson != null ? estadoJson : "{}";
        this.ativa = true;
        this.criadaEm = LocalDateTime.now();
    }

    // Construtor completo (para reconstrução de persistência)
    public Cena(UUID id, UUID sessaoId, String titulo, String mapaJson, String urlMapa, Integer ordem, String tipo, String estadoJson, Boolean ativa, LocalDateTime criadaEm, List<CenaParticipante> participantes) {
        this.id = Objects.requireNonNull(id, "O ID da cena é obrigatório");
        this.sessaoId = Objects.requireNonNull(sessaoId, "O ID da sessão é obrigatório");
        this.titulo = titulo != null ? titulo : "Cena " + ordem;
        this.mapaJson = mapaJson != null ? mapaJson : "{}";
        this.urlMapa = urlMapa != null ? urlMapa : "";
        this.ordem = ordem != null ? ordem : 1;
        this.tipo = tipo != null ? tipo : "PADRAO";
        this.estadoJson = estadoJson != null ? estadoJson : "{}";
        this.ativa = ativa != null ? ativa : true;
        this.criadaEm = criadaEm != null ? criadaEm : LocalDateTime.now();
        if (participantes != null) {
            this.participantes.addAll(participantes);
        }
    }

    public void adicionarParticipante(UUID instanciaId, String posicaoJson, Integer lado) {
        participantes.add(new CenaParticipante(instanciaId, posicaoJson, lado));
    }

    public void atualizarEstado(String titulo, String mapaJson, String urlMapa, Integer ordem, String tipo, String estadoJson, Boolean ativa) {
        if (titulo != null && !titulo.isBlank()) this.titulo = titulo;
        if (mapaJson != null) this.mapaJson = mapaJson;
        if (urlMapa != null) this.urlMapa = urlMapa;
        if (ordem != null) this.ordem = ordem;
        if (tipo != null) this.tipo = tipo;
        if (estadoJson != null) this.estadoJson = estadoJson;
        if (ativa != null) this.ativa = ativa;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getSessaoId() { return sessaoId; }
    public String getTitulo() { return titulo; }
    public String getMapaJson() { return mapaJson; }
    public String getUrlMapa() { return urlMapa; }
    public Integer getOrdem() { return ordem; }
    public String getTipo() { return tipo; }
    public String getEstadoJson() { return estadoJson; }
    public Boolean getAtiva() { return ativa; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public List<CenaParticipante> getParticipantes() { return Collections.unmodifiableList(participantes); }
}