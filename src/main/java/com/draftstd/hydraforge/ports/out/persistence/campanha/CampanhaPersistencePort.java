package com.draftstd.hydraforge.ports.out.persistence.campanha;

import com.draftstd.hydraforge.domain.model.campanha.Campanha;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampanhaPersistencePort {
    void salvar(Campanha campanha);
    Optional<Campanha> buscarPorId(UUID id);
    List<Campanha> listarTodas();
    List<Campanha> listarPorUsuario(UUID usuarioId);
    void deletar(UUID id);
}