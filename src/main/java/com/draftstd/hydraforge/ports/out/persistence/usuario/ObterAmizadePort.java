package com.draftstd.hydraforge.ports.out.persistence.usuario;

import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import com.draftstd.hydraforge.domain.model.usuario.Amizade;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObterAmizadePort {
    Optional<Amizade> buscarEntreUsuarios(UUID idA, UUID idB);
    List<Amizade> listarPorUsuarioEStatus(UUID usuarioId, StatusAmizade status);
    boolean existeEntreUsuarios(UUID idA, UUID idB);
}