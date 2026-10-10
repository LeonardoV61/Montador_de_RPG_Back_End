package com.draftstd.hydraforge.domain.model.entidade;

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
public class EntidadeRelacaoKey implements Serializable {

    @Column(name = "id_entidade_pai")
    private Long idEntidadePai;

    @Column(name = "id_entidade_filha")
    private Long idEntidadeFilha;
}