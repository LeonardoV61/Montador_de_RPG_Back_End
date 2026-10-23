package com.draftstd.hydraforge.application.request.sessao;

import java.util.UUID;

public record AdicionarEfeitoManualCommand(UUID sessaoId, UUID entidadeInstanciaId, UUID entidadeProcedimentoId, String parametrosJson, String momento, Integer expiraEm, Integer usosRestantes) {

}