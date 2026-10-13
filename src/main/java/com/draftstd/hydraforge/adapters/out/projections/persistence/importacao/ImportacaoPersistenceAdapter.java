package com.draftstd.hydraforge.adapters.out.projections.persistence.importacao;

import com.draftstd.hydraforge.adapters.in.dto.importacao.ImportacaoRequestDTO;
import com.draftstd.hydraforge.ports.in.query.importacao.ArquivoExportadoQueryResponse;
import com.draftstd.hydraforge.ports.in.query.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.ports.in.query.sistema.SistemaQueryResponse;
import com.draftstd.hydraforge.ports.out.persistence.importacao.ImportacaoOutputPort;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ImportacaoPersistenceAdapter implements ImportacaoOutputPort {

    @Override
    public Map<String, Long> processar(ImportacaoRequestDTO dto) {
        return Map.of();
    }

    @Override
    public PersonagemQueryResponse processarImportacaoPersonagem(byte[] conteudoArquivo, UUID usuarioId) {
        return null;
    }

    @Override
    public SistemaQueryResponse processarImportacaoSistema(byte[] conteudoArquivo, UUID usuarioId) {
        return null;
    }

    @Override
    public ArquivoExportadoQueryResponse gerarArquivoPersonagem(UUID personagemId) {
        return null;
    }

    @Override
    public ArquivoExportadoQueryResponse gerarArquivoSistema(UUID sistemaId) {
        return null;
    }
}