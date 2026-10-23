package com.draftstd.hydraforge.adapters.out.persistence.sistema.entity;

import tools.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entidade_instancia")
public class EntidadeInstanciaJpaEntity {

    @Id
    @Column(name = "id_instancia", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade")
    private EntidadeSistemaJpaEntity entidadeSistema;

    @Column(name = "id_campanha")
    private UUID campanhaId;

    @Column(name = "id_dono")
    private UUID donoId;

    @NotNull
    private String tipo;

    @NotNull
    private String nome;

    private String descricao;
    private String urlImagem;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode atributosAtuais;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode customizacoes;

    @CreationTimestamp
    @Column(name = "criada_em", updatable = false)
    private LocalDateTime criadaEm;
}