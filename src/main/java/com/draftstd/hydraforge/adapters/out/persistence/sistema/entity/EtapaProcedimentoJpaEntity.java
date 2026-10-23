package com.draftstd.hydraforge.adapters.out.persistence.sistema.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "etapas_procedimento")
public class EtapaProcedimentoJpaEntity {

    @Id
    @Column(name = "id_etapa", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID idEtapa;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_procedimento")
    private ProcedimentoJpaEntity procedimento;

    @NotNull
    private Integer ordem;

    @NotNull
    private String nome;

    @Column(name = "tipo_etapa")
    private String tipoEtapa;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parametros_etapa", columnDefinition = "jsonb")
    private JsonNode parametrosEtapa;

    @NotNull
    private Boolean obrigatorio;
}