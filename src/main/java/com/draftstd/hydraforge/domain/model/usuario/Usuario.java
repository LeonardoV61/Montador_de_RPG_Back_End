package com.draftstd.hydraforge.domain.model.usuario;

import com.draftstd.hydraforge.domain.model.campanha.CampanhaUsuario;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import com.draftstd.hydraforge.domain.model.sessao.MensagemLog;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator; // <-- Importante para gerar UUID no Hibernate
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID; // <-- Import do UUID

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {

        @Id
        @GeneratedValue
        @UuidGenerator // Garante a geração automática do UUID pelo Hibernate
        @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID") 
        private UUID id; // Alterado de Long para UUID

        private String senha;

        @NotBlank
        private String email;

        @NotBlank
        private String apelido;

        private String urlImagem; // Icone de perfil do usuário

        private boolean e_admin;

        // NOVOS CAMPOS PARA OAUTH2:
        private String provider;   // Salvará "GOOGLE" ou "DISCORD"
        private String providerId; // ID único que o Google/Discord gera para aquele usuário

        @CreationTimestamp
        @Column(name="criado_em", updatable = false)
        private LocalDateTime criadoEm;

        @UpdateTimestamp
        @Column(name="atualizado_em")
        private LocalDateTime atualizadoEm;

        @OneToMany(mappedBy = "usuario")
        private List<Sistema> sistema;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
        private List<Personagem> personagens;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
        private List<MensagemLog> mensagens;

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
        private List<CampanhaUsuario> campanhas;

        @OneToMany(mappedBy = "remetente", cascade = CascadeType.ALL)
        private List<Amizade> solicitacoesEnviadas;

        @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL)
        private List<Amizade> solicitacoesRecebidas;

        public String getNome() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getNome'");
        }
}