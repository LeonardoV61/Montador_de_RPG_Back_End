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
@Table(name = "entidade_procedimento")
public class EntidadeProcedimentoJpaEntity {

    @Id
    @Column(name = "id_entidade_procedimento", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema")
    private SistemaJpaEntity sistema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade")
    private EntidadeSistemaJpaEntity entidadeSistema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instancia")
    private EntidadeInstanciaJpaEntity entidadeInstancia;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_procedimento")
    private ProcedimentoJpaEntity procedimento;

    private String processamento;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode condicao;

    @NotNull
    @Column(name = "e_reativo")
    private boolean eReativo;

    private Integer ordem;
}