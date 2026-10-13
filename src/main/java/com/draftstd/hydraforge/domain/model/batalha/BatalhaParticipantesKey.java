package com.draftstd.hydraforge.domain.model.batalha;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID; // Importar o UUID

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class BatalhaParticipantesKey {

    @Column(name = "id_batalha")
    private UUID idBatalha; 

    @Column(name = "id_instancia")
    private UUID idInstancia; 
}