package com.draftstd.hydraforge.ports.out.persistence.usuario;

import com.draftstd.hydraforge.domain.model.usuario.Amizade;
import java.util.UUID;

public interface SalvarAmizadePort {
    void salvar(Amizade amizade);
    void deletar(UUID idRemetente, UUID idDestinatario);
}