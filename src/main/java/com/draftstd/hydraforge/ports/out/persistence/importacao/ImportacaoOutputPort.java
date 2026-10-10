package com.draftstd.hydraforge.ports.out.persistence.importacao;

import com.draftstd.hydraforge.adapters.in.dto.importacao.ImportacaoRequestDTO;

import java.util.Map;

public interface ImportacaoOutputPort {
    Map<String, Long> processar(ImportacaoRequestDTO dto);
}