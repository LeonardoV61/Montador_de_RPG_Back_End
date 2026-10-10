package com.draftstd.hydraforge.ports.out.personagem;

import com.draftstd.hydraforge.domain.model.personagem.Personagem;

public interface SalvarPersonagemPort {
    Personagem salvar(Personagem personagem);
}