package com.draftstd.hydraforge.ports.in.importacao;

import com.draftstd.hydraforge.ports.in.command.importacao.ExportarPersonagemCommand;
import com.draftstd.hydraforge.ports.in.query.importacao.ArquivoExportadoQueryResponse;

public interface ExportarPersonagemUseCase {
    ArquivoExportadoQueryResponse executar(ExportarPersonagemCommand command);
}