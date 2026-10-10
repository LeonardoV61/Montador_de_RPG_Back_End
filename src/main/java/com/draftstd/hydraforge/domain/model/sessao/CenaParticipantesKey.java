package com.draftstd.hydraforge.domain.model.sessao;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CenaParticipantesKey implements Serializable {

    @Column (name = "id_cena")
    private Long idCena;

    @Column (name = "id_instancia")
    private Long idInstancia;
}
