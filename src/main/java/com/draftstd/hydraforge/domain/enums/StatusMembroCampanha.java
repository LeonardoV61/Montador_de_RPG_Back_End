package com.draftstd.hydraforge.domain.enums;

public enum StatusMembroCampanha {
    PENDENTE,   // Convidado aguardando aceite
    ATIVO,      // Membro ativo participando da campanha
    SUSPENSO,   // Temporariamente afastado
    SAIU,       // Abandonou a campanha voluntariamente
    REMOVIDO    // Removido pelo Mestre
}