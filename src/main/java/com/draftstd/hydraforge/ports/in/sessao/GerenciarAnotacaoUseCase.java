package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.request.sessao.AtualizarAnotacaoCommand;
import com.draftstd.hydraforge.application.request.sessao.CriarAnotacaoCommand;
import java.util.UUID;

public interface GerenciarAnotacaoUseCase {
    UUID criarAnotacao(CriarAnotacaoCommand command);
    void atualizarAnotacao(AtualizarAnotacaoCommand command);
}