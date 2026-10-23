package com.draftstd.hydraforge.ports.in.importacao;

import com.draftstd.hydraforge.application.request.importacao.ExportarSistemaCommand;
import com.draftstd.hydraforge.application.response.importacao.ArquivoExportadoQueryResponse;

public interface ExportarSistemaUseCase {
    ArquivoExportadoQueryResponse executar(ExportarSistemaCommand command);
}