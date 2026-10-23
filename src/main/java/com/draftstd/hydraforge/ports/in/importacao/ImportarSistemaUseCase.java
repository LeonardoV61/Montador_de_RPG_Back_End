package com.draftstd.hydraforge.ports.in.importacao;

import com.draftstd.hydraforge.application.request.importacao.ImportarSistemaCommand;
import com.draftstd.hydraforge.application.response.sistema.SistemaQueryResponse;

public interface ImportarSistemaUseCase {
    SistemaQueryResponse executar(ImportarSistemaCommand command);
}