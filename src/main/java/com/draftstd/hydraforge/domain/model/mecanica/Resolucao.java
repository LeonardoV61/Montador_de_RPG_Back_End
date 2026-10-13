package com.draftstd.hydraforge.domain.model.mecanica;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Resolucao")
public class Resolucao {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id_etapa", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "Sistemas_id_sistema")
    private Sistema sistema;

    @NotNull
    private String nome;

    @NotNull
    private String tipo;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode parametros;
}