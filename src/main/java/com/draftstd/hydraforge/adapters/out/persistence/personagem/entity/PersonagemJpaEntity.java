package com.draftstd.hydraforge.adapters.out.persistence.personagem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "personagem")
public class PersonagemJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "id_usuario", nullable = false)
    private UUID usuarioId;

    @Column(name = "id_campanha")
    private UUID campanhaId;

    @Column(name = "id_instancia")
    private UUID instanciaId;

    @Column(columnDefinition = "TEXT")
    private String historia;

    @Column(nullable = false)
    private boolean ativo;

    private String aparencia;

    @Column(name = "url_imagem")
    private String urlImagem;

    @Column(name = "notas_jogador", columnDefinition = "TEXT")
    private String notasJogador;

    @Column(name = "atributos_json", columnDefinition = "TEXT")
    private String atributosJson;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;
}