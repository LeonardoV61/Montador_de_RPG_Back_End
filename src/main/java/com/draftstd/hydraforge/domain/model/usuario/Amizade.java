package com.draftstd.hydraforge.domain.model.usuario;

import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import java.time.LocalDateTime;
import java.util.UUID;

public class Amizade {

    private UUID idRemetente;
    private UUID idDestinatario;
    private StatusAmizade status;
    private LocalDateTime criadaEm;
    private LocalDateTime aceitaEm;

    public Amizade() {}

    public Amizade(UUID idRemetente, UUID idDestinatario, StatusAmizade status, LocalDateTime criadaEm, LocalDateTime aceitaEm) {
        this.idRemetente = idRemetente;
        this.idDestinatario = idDestinatario;
        this.status = status != null ? status : StatusAmizade.PENDENTE;
        this.criadaEm = criadaEm != null ? criadaEm : LocalDateTime.now();
        this.aceitaEm = aceitaEm;
    }

    public void aceitar() {
        this.status = StatusAmizade.ACEITO;
        this.aceitaEm = LocalDateTime.now();
    }

    public void rejeitar() {
        this.status = StatusAmizade.REJEITADO;
    }

    public void bloquear() {
        this.status = StatusAmizade.BLOQUEADO;
    }

    public UUID getIdRemetente() { return idRemetente; }
    public UUID getIdDestinatario() { return idDestinatario; }
    public StatusAmizade getStatus() { return status; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public LocalDateTime getAceitaEm() { return aceitaEm; }
}