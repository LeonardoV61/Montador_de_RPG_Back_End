package com.draftstd.hydraforge.application.response.sessao;

import java.util.UUID;

public record ObterMensagensLogQuery(UUID sessaoId, int limite) {

}