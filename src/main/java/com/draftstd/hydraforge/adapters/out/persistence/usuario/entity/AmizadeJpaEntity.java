package com.draftstd.hydraforge.adapters.out.persistence.usuario.entity;

import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "amizade")
public class AmizadeJpaEntity {

    @EmbeddedId
    private AmizadeKeyEmbeddable id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAmizade status;

    @CreationTimestamp
    @Column(name = "criada_em", updatable = false)
    private LocalDateTime criadaEm;

    @Column(name = "aceito_em")
    private LocalDateTime aceitoEm;
}