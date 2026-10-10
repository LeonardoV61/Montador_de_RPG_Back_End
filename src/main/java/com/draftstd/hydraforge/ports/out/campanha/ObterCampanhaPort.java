package com.draftstd.hydraforge.ports.out.campanha;

import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObterCampanhaPort {
    Optional<Campanha> buscarPorId(UUID id);
    List<Campanha> listarPorUsuarioId(UUID usuarioId);
}