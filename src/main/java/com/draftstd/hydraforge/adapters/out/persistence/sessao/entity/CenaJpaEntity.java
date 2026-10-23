package com.draftstd.hydraforge.adapters.out.persistence.sessao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cena")
public class CenaJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sessao", nullable = false)
    private SessaoJpaEntity sessao;

    @Column(name = "titulo")
    private String titulo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "mapa_json", columnDefinition = "jsonb")
    private String mapaJson;

    @Column(name = "url_mapa")
    private String urlMapa;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "estado", columnDefinition = "jsonb")
    private String estado;

    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    @CreationTimestamp
    @Column(name = "criada_em", nullable = false, updatable = false)
    private LocalDateTime criadaEm;
}