package com.rpgvtt.montador_de_rpg_backend.domain.model.entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Entidade_Relacao")
public class EntidadeRelacao {
    @EmbeddedId 
    private EntidadeRelacaoKey id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId("idEntidadePai") 
    @JoinColumn(name = "id_entidade_pai")
    private EntidadeInstancia entidadePai;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapsId("idEntidadeFilha") 
    @JoinColumn(name = "id_entidade_filha")
    private EntidadeInstancia entidadeFilha;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade_sistema_filha")
    private EntidadeSistema entidadeSistemaFilha;

    @NotNull
    private Integer quantidade;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode customizacoes;

    private String origem;
}
