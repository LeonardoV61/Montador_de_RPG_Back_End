package com.draftstd.hydraforge.application.usecase.importacao;

import com.draftstd.hydraforge.ports.in.command.importacao.ExportarPersonagemCommand;
import com.draftstd.hydraforge.ports.in.command.importacao.ExportarSistemaCommand;
import com.draftstd.hydraforge.ports.in.command.importacao.ImportarPersonagemCommand;
import com.draftstd.hydraforge.ports.in.command.importacao.ImportarSistemaCommand;
import com.draftstd.hydraforge.ports.in.importacao.ExportarPersonagemUseCase;
import com.draftstd.hydraforge.ports.in.importacao.ExportarSistemaUseCase;
import com.draftstd.hydraforge.ports.in.importacao.ImportarPersonagemUseCase;
import com.draftstd.hydraforge.ports.in.importacao.ImportarSistemaUseCase;
import com.draftstd.hydraforge.ports.in.query.importacao.ArquivoExportadoQueryResponse;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;
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
    public PersonagemQueryResponse importarPersonagem(ImportarPersonagemCommand command) {
        // Lógica de parsing e validação do esquema de ficheiro do personagem
        return importacaoOutputPort.processarImportacaoPersonagem(command.conteudoArquivo(), command.usuarioId());
    }

    @Override
    public SistemaQueryResponse importarSistema(ImportarSistemaCommand command) {
        // Lógica de parsing do ficheiro de regras do sistema de RPG
        return importacaoOutputPort.processarImportacaoSistema(command.conteudoArquivo(), command.usuarioId());
    }

    @Override
    public ArquivoExportadoQueryResponse exportarPersonagem(ExportarPersonagemCommand command) {
        // Lógica de serialização dos dados do personagem para formato transferível
        return importacaoOutputPort.gerarArquivoPersonagem(command.personagemId());
    }

    @Override
    public ArquivoExportadoQueryResponse exportarSistema(ExportarSistemaCommand command) {
        // Lógica de serialização das regras do sistema para formato transferível
        return importacaoOutputPort.gerarArquivoSistema(command.sistemaId());
    }
}