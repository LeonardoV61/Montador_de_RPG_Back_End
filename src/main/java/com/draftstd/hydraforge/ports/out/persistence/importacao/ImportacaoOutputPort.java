package com.draftstd.hydraforge.ports.out.persistence.importacao;

import com.draftstd.hydraforge.adapters.in.dto.importacao.ImportacaoRequestDTO;
import com.draftstd.hydraforge.application.response.importacao.ArquivoExportadoQueryResponse;
import com.draftstd.hydraforge.application.response.personagem.PersonagemQueryResponse;
import com.draftstd.hydraforge.application.response.sistema.SistemaQueryResponse;

import java.util.Map;
import java.util.UUID;

public interface ImportacaoOutputPort {
    Map<String, UUID> processar(ImportacaoRequestDTO dto);
    PersonagemQueryResponse processarImportacaoPersonagem(byte[] conteudoArquivo, UUID usuarioId);
    SistemaQueryResponse processarImportacaoSistema(byte[] conteudoArquivo, UUID usuarioId);
    ArquivoExportadoQueryResponse gerarArquivoPersonagem(UUID personagemId);
    ArquivoExportadoQueryResponse gerarArquivoSistema(UUID sistemaId);
}