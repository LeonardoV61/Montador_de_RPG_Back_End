package com.draftstd.hydraforge.adapters.out.persistence.importacao.adapter;

import com.draftstd.hydraforge.adapters.in.dto.importacao.ImportacaoRequestDTO;
import com.draftstd.hydraforge.application.response.importacao.ArquivoExportadoQueryResponse;
import com.draftstd.hydraforge.application.response.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.application.response.sistema.SistemaQueryResponse;
import com.draftstd.hydraforge.ports.out.persistence.importacao.ImportacaoOutputPort;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ImportacaoPersistenceAdapter implements ImportacaoOutputPort {

    @Override
    public Map<String, UUID> processar(ImportacaoRequestDTO dto) {
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