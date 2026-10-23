package com.draftstd.hydraforge.adapters.out.persistence.usuario.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AmizadeKeyEmbeddable implements Serializable {
    private UUID idRemetente;
    private UUID idDestinatario;
}