package com.draftstd.hydraforge.application.usecase.importacao;

import com.draftstd.hydraforge.application.request.importacao.*;
import com.draftstd.hydraforge.ports.in.importacao.*;
import com.draftstd.hydraforge.application.response.importacao.ArquivoExportadoQueryResponse;
import com.draftstd.hydraforge.application.response.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.application.response.sistema.SistemaQueryResponse;
import com.draftstd.hydraforge.ports.out.persistence.importacao.ImportacaoOutputPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcessarImportacaoUseCaseImpl implements ImportarPersonagemUseCase, ImportarSistemaUseCase, ExportarPersonagemUseCase, ExportarSistemaUseCase {

    private final ImportacaoOutputPort importacaoOutputPort;

    public ProcessarImportacaoUseCaseImpl(ImportacaoOutputPort importacaoOutputPort) {
        this.importacaoOutputPort = importacaoOutputPort;
    }

    @Override
    public PersonagemQueryResponse executar(ImportarPersonagemCommand command) {
        return importacaoOutputPort.processarImportacaoPersonagem(command.conteudoArquivo(), command.usuarioId());
    }

    @Override
    public SistemaQueryResponse executar(ImportarSistemaCommand command) {
        return importacaoOutputPort.processarImportacaoSistema(command.conteudoArquivo(), command.autorId());
    }

    @Override
    public ArquivoExportadoQueryResponse executar(ExportarPersonagemCommand command) {
        return importacaoOutputPort.gerarArquivoPersonagem(command.personagemId());
    }

    @Override
    public ArquivoExportadoQueryResponse executar(ExportarSistemaCommand command) {
        return importacaoOutputPort.gerarArquivoSistema(command.sistemaId());
    }
}