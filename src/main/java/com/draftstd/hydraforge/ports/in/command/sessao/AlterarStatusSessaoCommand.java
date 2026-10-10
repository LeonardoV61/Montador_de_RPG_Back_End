package com.draftstd.hydraforge.ports.in.command.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import java.util.UUID;

public record AlterarStatusSessaoCommand(UUID sessaoId, StatusSessao novoStatus) {

}