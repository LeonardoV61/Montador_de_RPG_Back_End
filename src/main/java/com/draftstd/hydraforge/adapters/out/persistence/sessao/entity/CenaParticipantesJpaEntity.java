package com.draftstd.hydraforge.adapters.out.persistence.sessao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cena_participantes")
public class CenaParticipantesJpaEntity {

    @EmbeddedId
    private CenaParticipantesKeyJpaEntity id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("idCena")
    @JoinColumn(name = "id_cena", nullable = false)
    private CenaJpaEntity cena;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "posicao_json", columnDefinition = "jsonb")
    private String posicaoJson;

    @Column(name = "lado")
    private Integer lado;
}