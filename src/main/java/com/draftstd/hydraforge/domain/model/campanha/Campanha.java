package com.draftstd.hydraforge.domain.model.campanha;

import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;

// import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Campanha {

        @Id
        @GeneratedValue
        @UuidGenerator
        @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
        private UUID id;

        @NotNull
        private String nome;

        private String descricao;

        private String urlImagem;

        @NotNull
        // @Enumerated(EnumType.STRING)
        @Column(name = "status")
        private StatusCampanha status;

        @CreationTimestamp
        @Column(name = "criada_em")
        private LocalDateTime criadaEm;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "id_sistema")
        private Sistema sistema;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "campanha")
        private List<Sessao> sessoes;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "campanha")
        private List<Personagem> personagens;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "campanha")
        private List<CampanhaUsuario> usuarios;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "campanha")
        private List<EntidadeInstancia> entidadeInstancias;
}
