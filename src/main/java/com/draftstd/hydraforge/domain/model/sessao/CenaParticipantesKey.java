package com.draftstd.hydraforge.domain.model.sessao;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CenaParticipantesKey implements Serializable {

    @Column (name = "id_cena")
    private UUID idCena;

   @Column(name = "id_instancia")
    private UUID idInstancia;
}
