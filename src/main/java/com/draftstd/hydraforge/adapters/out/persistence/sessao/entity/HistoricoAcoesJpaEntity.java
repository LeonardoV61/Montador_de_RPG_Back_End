package com.draftstd.hydraforge.adapters.out.persistence.sessao.entity;

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
@Table(name = "historico_acoes")
public class HistoricoAcoesJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sessao", nullable = false)
    private SessaoJpaEntity sessao;

    @Column(name = "entidade_instancia_id", nullable = false, columnDefinition = "UUID")
    private UUID entidadeInstanciaId;

    @CreationTimestamp
    @Column(name = "momento", nullable = false, updatable = false)
    private LocalDateTime momento;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "resultado_json", columnDefinition = "jsonb")
    private String resultadoJson;
}