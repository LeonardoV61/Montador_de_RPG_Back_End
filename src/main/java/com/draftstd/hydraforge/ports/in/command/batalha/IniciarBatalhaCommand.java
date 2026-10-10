package com.draftstd.hydraforge.ports.in.command.batalha;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record IniciarBatalhaCommand(@NotNull(message = "O ID da sessão é obrigatório") Long idSessao, Long idCena, @NotNull(message = "A lista de instâncias de jogadores é obrigatória") List<Long> idsInstanciasJogadores,@NotNull(message = "A lista de instâncias de inimigos é obrigatória") List<Long> idsInstanciasInimigos, List<List<Long>> lados) {

}
