package com.draftstd.hydraforge.ports.in.importacao;

import com.draftstd.hydraforge.application.request.importacao.ExportarPersonagemCommand;
import com.draftstd.hydraforge.application.response.importacao.ArquivoExportadoQueryResponse;

public interface ExportarPersonagemUseCase {
    ArquivoExportadoQueryResponse executar(ExportarPersonagemCommand command);
}