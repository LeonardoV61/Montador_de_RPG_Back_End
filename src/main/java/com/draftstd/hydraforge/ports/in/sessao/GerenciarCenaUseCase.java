package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.request.sessao.AtualizarCenaCommand;
import com.draftstd.hydraforge.application.request.sessao.CriarCenaCommand;
import java.util.UUID;

public interface GerenciarCenaUseCase {
    UUID criarCena(CriarCenaCommand command);
    void atualizarCena(AtualizarCenaCommand command);
}