package com.draftstd.hydraforge.domain.model.sistema;

import com.draftstd.hydraforge.domain.enums.StatusProcedimento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import com.draftstd.hydraforge.domain.model.mecanica.EntidadeProcedimento;
import tools.jackson.databind.JsonNode;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Procedimento {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne (optional = false, fetch = FetchType.LAZY)
    @JoinColumn (name = "id_sistema")
    private Sistema sistema;

    @NotNull
    private String tipo;

    @NotNull
    private String nome;

    private String descricao;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private StatusProcedimento status;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode configsGeral;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procedimento")
    private List<EtapaProcedimento> etapas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procedimento")
    private List<EntidadeProcedimento> procedimentos;
}