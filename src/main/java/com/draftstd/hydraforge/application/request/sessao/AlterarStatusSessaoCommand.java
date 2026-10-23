package com.draftstd.hydraforge.application.request.sessao;

import com.draftstd.hydraforge.domain.enums.StatusSessao;
import java.util.UUID;

public record AlterarStatusSessaoCommand(UUID sessaoId, StatusSessao novoStatus, String snapshotFinalJson) {

}