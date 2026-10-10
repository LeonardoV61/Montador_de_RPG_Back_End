package com.draftstd.hydraforge.ports.out.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Sessao;

public interface SalvarSessaoPort {
    Sessao salvar(Sessao sessao);
}