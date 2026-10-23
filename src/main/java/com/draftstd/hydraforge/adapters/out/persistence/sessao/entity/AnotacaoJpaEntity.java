package com.draftstd.hydraforge.adapters.out.persistence.sessao.entity;

import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.UsuarioJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "anotacoes")
public class AnotacaoJpaEntity {

    @Id
    @Column(name = "id_anotacao", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID idAnotacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_autor", nullable = false)
    private UsuarioJpaEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campanhas_id_campanha", nullable = false)
    private CampanhaJpaEntity campanha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessoes_id_sessao")
    private SessaoJpaEntity sessao;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "conteudo", columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "categoria")
    private String categoria;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "anotacao_complexa", columnDefinition = "jsonb")
    private String anotacaoComplexa;

    @Column(name = "e_privado", nullable = false)
    private Boolean ePrivado;

    @CreationTimestamp
    @Column(name = "criada_em", nullable = false, updatable = false)
    private LocalDateTime criadaEm;
}