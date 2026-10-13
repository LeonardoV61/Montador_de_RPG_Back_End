package com.draftstd.hydraforge.domain.model.campanha;

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
public class CampanhaUsuarioKey implements Serializable {

    @Column (name = "id_campanha")
    private UUID idCampanha;

    @Column (name = "id_usuario")
    private UUID idUsuario;

}
