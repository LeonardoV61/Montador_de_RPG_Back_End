package com.draftstd.hydraforge.ports.out.persistence.mecanica;

import com.draftstd.hydraforge.domain.model.mecanica.Resolucao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResolucaoOutputPort {
    Resolucao salvar(Resolucao resolucao);
    Optional<Resolucao> buscarPorId(UUID id);
    List<Resolucao> listarPorSistema(UUID sistemaId);
    void deletar(UUID id);
}