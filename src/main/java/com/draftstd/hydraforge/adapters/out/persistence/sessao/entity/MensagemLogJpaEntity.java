package com.draftstd.hydraforge.adapters.out.persistence.sessao.entity;

import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.UsuarioJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mensagens_log")
public class MensagemLogJpaEntity {

    @Id
    @Column(name = "id_log", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sessoes_id_sessao", nullable = false)
    private SessaoJpaEntity sessao;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarios_id_usuario", nullable = false)
    private UsuarioJpaEntity usuario;

    @Column(name = "conteudo", nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @CreationTimestamp
    @Column(name = "momento", nullable = false, updatable = false)
    private LocalDateTime momento;
}