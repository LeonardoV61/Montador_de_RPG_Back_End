package com.draftstd.hydraforge.adapters.out.persistence.sessao.entity;

import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaJpaEntity;
import com.draftstd.hydraforge.domain.enums.StatusSessao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sessao")
public class SessaoJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_campanha", nullable = false)
    private CampanhaJpaEntity campanha;

    @CreationTimestamp
    @Column(name = "data_inicio", nullable = false, updatable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "sessao_snapshot", columnDefinition = "jsonb")
    private String sessaoSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusSessao status;

    @Column(name = "ordem", nullable = false)
    private int ordem;

    @Column(name = "procedimento_ativo_id", columnDefinition = "UUID")
    private UUID procedimentoAtivoId;
}