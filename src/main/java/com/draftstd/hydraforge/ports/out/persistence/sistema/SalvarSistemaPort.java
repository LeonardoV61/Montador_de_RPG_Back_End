package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import java.util.UUID;

public interface SalvarSistemaPort {
    void salvar(Sistema sistema);
    void deletarPorId(UUID id);
}