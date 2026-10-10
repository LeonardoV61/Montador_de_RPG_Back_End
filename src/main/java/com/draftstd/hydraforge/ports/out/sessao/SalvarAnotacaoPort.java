package com.draftstd.hydraforge.ports.out.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Anotacao;

public interface SalvarAnotacaoPort {
    Anotacao salvar(Anotacao anotacao);
}