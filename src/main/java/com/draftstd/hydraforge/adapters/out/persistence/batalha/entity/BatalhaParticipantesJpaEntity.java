package com.draftstd.hydraforge.adapters.out.persistence.batalha.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeInstanciaJpaEntity;

import tools.jackson.databind.JsonNode;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "batalha_participantes")
public class BatalhaParticipantesJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_batalha", nullable = false)
    private BatalhaJpaEntity batalha;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instancia", nullable = false)
    private EntidadeInstanciaJpaEntity entidadeInstancia;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode posicao;

    private Integer lado;

    @Column(name = "ordem_iniciativa")
    private Integer ordemIniciativa;

    @Column(name = "resultado_iniciativa")
    private Integer resultadoIniciativa;

    @Column(name = "ativo")
    private boolean ativo;
}
