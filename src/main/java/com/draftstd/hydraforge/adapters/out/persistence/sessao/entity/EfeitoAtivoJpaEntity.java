package com.draftstd.hydraforge.adapters.out.persistence.sessao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "efeitos_ativos")
public class EfeitoAtivoJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sessao", nullable = false)
    private SessaoJpaEntity sessao;

    @Column(name = "entidade_instancia_id", nullable = false, columnDefinition = "UUID")
    private UUID entidadeInstanciaId;

    @Column(name = "entidade_procedimento_id", columnDefinition = "UUID")
    private UUID entidadeProcedimentoId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parametros_json", columnDefinition = "jsonb")
    private String parametrosJson;

    @Column(name = "momento", nullable = false)
    private String momento;

    @Column(name = "expira_em")
    private Integer expiraEm; // Corrected from LocalDateTime to Integer

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "usos_restantes")
    private Integer usosRestantes;
}