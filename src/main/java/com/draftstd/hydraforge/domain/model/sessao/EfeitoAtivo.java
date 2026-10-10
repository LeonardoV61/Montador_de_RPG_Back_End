package com.draftstd.hydraforge.domain.model.sessao;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.mecanica.EntidadeProcedimento;

// import com.draftstd.hydraforge.domain.model.mecanica.EntidadeEfeito;
// import com.draftstd.hydraforge.domain.model.personagem.Personagem;
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
@Table (name = "efeito_ativo")
public class EfeitoAtivo {
        @Id
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "efAt_seq"
        )
        @SequenceGenerator(
                name = "efAt_seq",
                sequenceName = "efAt_sequence",
                allocationSize = 1
        )
        private Long id;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "id_instancia")
        private EntidadeInstancia entidadeInstancia;

        @ManyToOne (optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "id_entidade_procedimento")
        private EntidadeProcedimento entidadeProcedimento;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "id_sessao")
        private Sessao sessao;

        @NotNull
        @JdbcTypeCode(SqlTypes.JSON)
        @Column(columnDefinition = "jsonb")
        private JsonNode parametros;

        @NotNull
        @Column(name = "momento_aplicacao")
        private String momento;

        @NotNull
        private Integer expiraEm;

        @NotNull
        private boolean ativo;

        @Column(name = "usos_restantes")
        private Integer usosRestantes;


}
