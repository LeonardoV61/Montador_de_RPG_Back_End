package com.draftstd.hydraforge.adapters.out.persistence.batalha.entity;

import com.draftstd.hydraforge.domain.enums.BatalhaStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter 
@Setter
@NoArgsConstructor
@Entity
@Table(name = "batalha")
public class BatalhaJpaEntity {

@Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "sessao_id", nullable = false)
    private UUID sessaoId;

    @Column(name = "cena_id")
    private UUID cenaId;

    @Column(name = "versao")
    private Long versao;

    @Column(name = "rodada_atual")
    private int rodadaAtual;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BatalhaStatus status;

    @Column(name = "iniciada_em")
    private LocalDateTime iniciadaEm;

    @OneToMany(mappedBy = "batalha", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordemIniciativa ASC")
    private List<BatalhaParticipantesJpaEntity> participantes;
}
