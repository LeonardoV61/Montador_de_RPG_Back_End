package com.draftstd.hydraforge.adapters.out.persistence.sistema.entity;

import tools.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entidade_relacao")
public class EntidadeRelacaoJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_entidade_pai")
    private EntidadeInstanciaJpaEntity entidadePai;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_entidade_filha")
    private EntidadeInstanciaJpaEntity entidadeFilha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade_sistema_filha")
    private EntidadeSistemaJpaEntity entidadeSistemaFilha;

    @NotNull
    private Integer quantidade;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode customizacoes;

    private String origem;
}