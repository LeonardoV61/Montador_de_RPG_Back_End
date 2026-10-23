package com.draftstd.hydraforge.adapters.out.persistence.campanha.entity;

import com.draftstd.hydraforge.domain.enums.StatusCampanha;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "campanha")
public class CampanhaJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(name = "url_imagem")
    private String urlImagem;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusCampanha status;

    @Column(name = "id_sistema", nullable = false, columnDefinition = "UUID")
    private UUID sistemaId;

    @Column(name = "id_mestre", nullable = false, columnDefinition = "UUID")
    private UUID mestreId;

    @Column(name = "version", nullable = false)
    private long version = 0L;

    @CreationTimestamp
    @Column(name = "criada_em", updatable = false)
    private LocalDateTime criadaEm;

    @Column(name = "atualizada_em")
    private LocalDateTime atualizadaEm;

    @OneToMany(mappedBy = "campanha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampanhaMembroJpaEntity> membros = new ArrayList<>();
}