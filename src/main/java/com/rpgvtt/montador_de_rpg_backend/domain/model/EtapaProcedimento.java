package com.rpgvtt.montador_de_rpg_backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Etapas_Procedimento")
public class EtapaProcedimento {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE, 
        generator = "evento_sistema_seq"
    )
    @SequenceGenerator(
        name = "evento_sistema_seq", 
        sequenceName = "evento_sistema_sequence", 
        allocationSize = 1
    )
    @Column(name = "id_etapa")
    private Integer idEtapa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Procedimentos_id_procedimento")
    private Procedimento procedimento;

    private Interger ordem;

    private String nome;

    //tipo_etapa enum

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode parametros_etapa;

    private Boolean obrigatorio;
}