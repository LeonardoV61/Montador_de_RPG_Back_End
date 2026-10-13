package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProcedimentoOutputPort {
    Procedimento salvar(Procedimento procedimento);
    Optional<Procedimento> buscarPorId(UUID id);
    List<Procedimento> listarPorSistema(UUID sistemaId);
    List<Procedimento> listarTodos();
    void deletarPorId(UUID id);
    boolean existePorId(UUID id);
}