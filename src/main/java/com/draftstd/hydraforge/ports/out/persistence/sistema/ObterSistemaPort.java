package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObterSistemaPort {
    Optional<Sistema> buscarPorId(UUID id);
    List<Sistema> listarTodos();
}