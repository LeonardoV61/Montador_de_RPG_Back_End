package com.draftstd.hydraforge.domain.model.mecanica;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ResultadoRolagem {
    private final UUID id;
    private final UUID sessaoId;
    private final UUID personagemId;
    private final String expressao; // Ex: "2d20 + 5"
    private final List<Integer> dadosSorteados; // Ex: [14, 18]
    private final int modificador;
    private final int total;
    private final String tipoRolagem; // Ex: "ATAQUE", "TESTE_RESISTENCIA"
    private final LocalDateTime dataHora;

    public ResultadoRolagem(UUID id, UUID sessaoId, UUID personagemId, String expressao, 
                            List<Integer> dadosSorteados, int modificador, int total, 
                            String tipoRolagem, LocalDateTime dataHora) {
        this.id = id != null ? id : UUID.randomUUID();
        this.sessaoId = Objects.requireNonNull(sessaoId, "A sessão é obrigatória para a rolagem.");
        this.personagemId = personagemId; // Pode ser nulo se for uma rolagem genérica do Mestre
        this.expressao = Objects.requireNonNull(expressao, "A expressão da rolagem não pode ser nula.");
        this.dadosSorteados = dadosSorteados != null ? List.copyOf(dadosSorteados) : List.of();
        this.modificador = modificador;
        this.total = total;
        this.tipoRolagem = tipoRolagem;
        this.dataHora = dataHora != null ? dataHora : LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getSessaoId() { return sessaoId; }
    public UUID getPersonagemId() { return personagemId; }
    public String getExpressao() { return expressao; }
    public List<Integer> getDadosSorteados() { return dadosSorteados; }
    public int getModificador() { return modificador; }
    public int getTotal() { return total; }
    public String getTipoRolagem() { return tipoRolagem; }
    public LocalDateTime getDataHora() { return dataHora; }
}