package com.draftstd.hydraforge.ports.out.persistence.personagem;

import com.draftstd.hydraforge.domain.model.personagem.Personagem;

public interface SalvarPersonagemPort {
    void salvar(Personagem personagem);
}