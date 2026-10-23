package com.draftstd.hydraforge.ports.out.persistence.personagem;

import java.util.UUID;

public interface DeletarPersonagemPort {
    void deletarPorId(UUID id);
}