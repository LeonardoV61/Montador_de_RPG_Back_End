package com.draftstd.hydraforge.domain.model.campanha;

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
public class CampanhaUsuarioKey implements Serializable {

    @Column (name = "id_campanha")
    private Long idCampanha;

    @Column (name = "id_usuario")
    private Long idUsuario;

}
