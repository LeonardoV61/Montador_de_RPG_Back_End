package com.draftstd.hydraforge.domain.model.sessao;

// import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
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

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cena {
        @Id
        @GeneratedValue
        @UuidGenerator
        @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
        private UUID id;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "id_sessao")
        private Sessao sessao;

        @NotNull
        @JdbcTypeCode(SqlTypes.JSON)
        @Column(columnDefinition = "jsonb")
        private JsonNode mapaJson;

        private String urlMapa;

        @NotNull
        private Integer ordem;

        @NotNull
        private String tipo; // Ex: Combate

        @OneToMany(mappedBy = "cena",  cascade =  CascadeType.ALL, orphanRemoval = true)
        @OrderBy("ordemIniciativa ASC")
        private List<CenaParticipantes> participantes;

        @JdbcTypeCode(SqlTypes.JSON)
        @Column(columnDefinition = "jsonb")
        private JsonNode estado;

                // {
                //         "combateAtivo": true,
                //         "rodada": 1,
                //         "turnoAtualId": 42,
                //         "jaAgiramIds": [42, 107],
                //         "dadosDisponiveis": {
                //                 "42": [5, 6, 2, 4]
                //         }
                // }

}
