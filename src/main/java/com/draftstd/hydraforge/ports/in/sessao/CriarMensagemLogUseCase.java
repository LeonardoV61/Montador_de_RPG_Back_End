package com.draftstd.hydraforge.ports.in.sessao;

import com.draftstd.hydraforge.application.request.sessao.CriarMensagemLogCommand;
import java.util.UUID;

public interface CriarMensagemLogUseCase {
    UUID executar(CriarMensagemLogCommand command);
}