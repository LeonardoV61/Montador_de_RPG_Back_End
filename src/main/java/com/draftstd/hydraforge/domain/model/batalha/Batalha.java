package com.draftstd.hydraforge.domain.model.batalha;

import com.draftstd.hydraforge.domain.enums.BatalhaStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "batalha")
public class Batalha {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "sessao_id", nullable = false)
    private UUID sessaoId;

    @Column(name = "cena_id")
    private UUID cenaId;

    @Column(name = "versao")
    private Long versao;

    @Column(name = "rodada_atual")
    private int rodadaAtual;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BatalhaStatus status;

    @Column(name = "iniciada_em")
    private LocalDateTime iniciadaEm;

    @OneToMany(mappedBy = "batalha", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordemIniciativa ASC")
    private List<BatalhaParticipantes> participantes;
    
    public List<BatalhaParticipantes> getParticipantes() {
        return this.participantes;
    }

    public List<BatalhaParticipantes> participantesAtivos() {
        return participantes.stream()
                .filter(BatalhaParticipantes::isAtivo)
                .toList();
    }

    public List<BatalhaParticipantes> participantesLado(int lado) {
        return participantes.stream()
                .filter(p -> p.getLado() == lado && p.isAtivo())
                .toList();
    }


    public boolean haParticipanteAtivoNoLado(int lado) {
        return participantes.stream()
                .anyMatch(p -> p.getLado() == lado && p.isAtivo());
    }

    public Object toDomain() {
        // Método de mapeamento opcional para o domínio puro se necessário
        return this;
    }
}