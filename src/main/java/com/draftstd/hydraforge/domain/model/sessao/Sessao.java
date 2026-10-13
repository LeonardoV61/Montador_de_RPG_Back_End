package com.draftstd.hydraforge.domain.model.sessao;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import com.draftstd.hydraforge.domain.enums.StatusSessao;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.domain.model.sistema.Procedimento;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sessao {
        @Id
        @GeneratedValue
        @UuidGenerator
        @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
        private UUID id;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "id_campanha")
        private Campanha campanha;

        @CreationTimestamp
        @Column(name = "data_inicio", nullable = false, updatable = false)
        private LocalDateTime dataInicio;

        @CreationTimestamp
        @Column(name = "data_fim", nullable = false, updatable = false)
        private LocalDateTime dataFim;

        @JdbcTypeCode(SqlTypes.JSON)
        @Column(columnDefinition = "jsonb")
        private JsonNode sessaoSnapshot;

        @NotNull
        // @Enumerated(EnumType.STRING)
        @Column(name = "status")
        private StatusSessao status;

        private int ordem;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id_procedimento_ativo")
        private Procedimento procedimentoAtivo;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessao")
        private List<Cena> cenas;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessao")
        private List<HistoricoAcoes> historicoAcoes;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "sessao")
        private List<MensagemLog> mensagens;
}