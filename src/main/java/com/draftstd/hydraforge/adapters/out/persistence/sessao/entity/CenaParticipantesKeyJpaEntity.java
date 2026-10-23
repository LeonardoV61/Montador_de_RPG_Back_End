package com.draftstd.hydraforge.adapters.out.persistence.sessao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CenaParticipantesKeyJpaEntity implements Serializable {

    @Column(name = "id_cena", nullable = false, columnDefinition = "UUID")
    private UUID idCena;

    @Column(name = "instancia_id", nullable = false, columnDefinition = "UUID")
    private UUID instanciaId;
}