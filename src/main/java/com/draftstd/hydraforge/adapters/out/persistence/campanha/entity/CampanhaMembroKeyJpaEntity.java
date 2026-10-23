package com.draftstd.hydraforge.adapters.out.persistence.campanha.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CampanhaMembroKeyJpaEntity implements Serializable {

    @Column(name = "id_campanha")
    private UUID idCampanha;

    @Column(name = "id_usuario")
    private UUID idUsuario;
}