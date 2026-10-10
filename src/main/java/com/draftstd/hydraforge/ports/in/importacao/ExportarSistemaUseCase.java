package com.draftstd.hydraforge.ports.in.importacao;

import com.draftstd.hydraforge.ports.in.command.importacao.ExportarSistemaCommand;
import com.draftstd.hydraforge.ports.in.query.importacao.ArquivoExportadoQueryResponse;

public interface ExportarSistemaUseCase {
    ArquivoExportadoQueryResponse executar(ExportarSistemaCommand command);
}