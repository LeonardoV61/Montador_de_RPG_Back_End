package com.draftstd.hydraforge.domain.enums;

public enum StatusCampanha {
    EM_PREPARACAO, // Campanha criada pelo Mestre, em fase de configuração/recrutamento
    ATIVA,         // Campanha ativa e em andamento regular
    PAUSADA,       // Campanha temporariamente paralisada
    FINALIZADA,    // História concluída/encerrada com sucesso
    CANCELADA      // Campanha cancelada/descontinuada
}