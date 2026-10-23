package com.draftstd.hydraforge.adapters.out.persistence.campanha.entity;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.enums.StatusMembroCampanha;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "campanha_membro")
public class CampanhaMembroJpaEntity {

    @EmbeddedId
    private CampanhaMembroKeyJpaEntity id;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel", nullable = false)
    private PapeisUsuario papel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusMembroCampanha status;

    @CreationTimestamp
    @Column(name = "entrou_em")
    private LocalDateTime entrouEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCampanha")
    @JoinColumn(name = "id_campanha")
    private CampanhaJpaEntity campanha;
}