package com.draftstd.hydraforge.ports.in.importacao;

import com.draftstd.hydraforge.ports.in.command.importacao.ImportarSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;

public interface ImportarSistemaUseCase {
    SistemaQueryResponse executar(ImportarSistemaCommand command);
}