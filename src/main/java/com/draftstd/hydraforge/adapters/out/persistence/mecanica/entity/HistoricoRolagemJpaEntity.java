package com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/** Modelo de leitura (projecao) do historico de rolagens. O registro de verdade fica no event store. */
@Entity
@Table(name = "historico_rolagem", indexes = {
        @Index(name = "idx_historico_rolagem_sessao_data", columnList = "sessao_id, criado_em")
})
public class HistoricoRolagemJpaEntity {

    @Id
    private UUID id;

    @Column(name = "sessao_id", nullable = false)
    private UUID sessaoId;

    @Column(name = "jogador_id")
    private UUID jogadorId;

    @Column(name = "personagem_id")
    private UUID personagemId;

    @Column(name = "expressao_dados", length = 255)
    private String expressaoDados;

    /** Valores dos dados separados por virgula, ex.: "15,3". */
    @Column(name = "resultados", length = 1000)
    private String resultados;

    private Integer modificador;

    private Integer total;

    @Column(length = 500)
    private String motivo;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    protected HistoricoRolagemJpaEntity() {
    }

    public HistoricoRolagemJpaEntity(UUID id, UUID sessaoId, UUID jogadorId, UUID personagemId,
                                  String expressaoDados, String resultados, Integer modificador,
                                  Integer total, String motivo, LocalDateTime criadoEm) {
        this.id = id;
        this.sessaoId = sessaoId;
        this.jogadorId = jogadorId;
        this.personagemId = personagemId;
        this.expressaoDados = expressaoDados;
        this.resultados = resultados;
        this.modificador = modificador;
        this.total = total;
        this.motivo = motivo;
        this.criadoEm = criadoEm;
    }

    public UUID getId() { return id; }
    public UUID getSessaoId() { return sessaoId; }
    public UUID getJogadorId() { return jogadorId; }
    public UUID getPersonagemId() { return personagemId; }
    public String getExpressaoDados() { return expressaoDados; }
    public String getResultados() { return resultados; }
    public Integer getModificador() { return modificador; }
    public Integer getTotal() { return total; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}