package com.draftstd.hydraforge.ports.in.command.batalha;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record IniciarBatalhaCommand(@NotNull(message = "O ID da sessão é obrigatório") UUID idSessao, UUID idCena, @NotNull(message = "A lista de instâncias de jogadores é obrigatória") List<UUID> idsInstanciasJogadores, @NotNull(message = "A lista de instâncias de inimigos é obrigatória") List<UUID> idsInstanciasInimigos, List<List<UUID>> lados) {

        public UUID sessaoId() {
            return idSessao;
        }
        
        public UUID cenaId() {
            return idCena;
        }
    }