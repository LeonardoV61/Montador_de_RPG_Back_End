package com.draftstd.hydraforge.domain.model.entidade;

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
public class EntidadeRelacaoKey implements Serializable {

    @Column(name = "id_entidade_pai", columnDefinition = "UUID")
    private UUID idEntidadePai;

    @Column(name = "id_entidade_filha", columnDefinition = "UUID")
    private UUID idEntidadeFilha;
}